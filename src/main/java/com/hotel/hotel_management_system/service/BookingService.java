package com.hotel.hotel_management_system.service;

import com.hotel.hotel_management_system.dto.BookingRequest;
import com.hotel.hotel_management_system.dto.BookingResponse;
import com.hotel.hotel_management_system.exception.ResourceNotFoundException;
import com.hotel.hotel_management_system.exception.ValidationException;
import com.hotel.hotel_management_system.model.Booking;
import com.hotel.hotel_management_system.model.Guest;
import com.hotel.hotel_management_system.model.Room;
import com.hotel.hotel_management_system.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomService roomService;
    private final GuestService guestService;
    private final PaymentService paymentService;

    public List<Booking> getAllBookings() {
        log.info("Fetching all bookings");
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        log.info("Fetching booking with id: {}", id);
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
    }

    public List<Booking> getBookingsByGuest(Long guestId) {
        log.info("Fetching bookings for guest id: {}", guestId);
        return bookingRepository.findByGuestId(guestId);
    }

    public List<Booking> getActiveBookingsByGuest(Long guestId) {
        log.info("Fetching active bookings for guest id: {}", guestId);
        return bookingRepository.findActiveBookingsByGuestId(guestId);
    }

    public List<Booking> getBookingsByRoom(Long roomId) {
        log.info("Fetching bookings for room id: {}", roomId);
        return bookingRepository.findByRoomId(roomId);
    }

    public List<Booking> getBookingsByStatus(String status) {
        log.info("Fetching bookings with status: {}", status);
        return bookingRepository.findByBookingStatus(status);
    }

    public List<Booking> getBookingsForDate(LocalDate date) {
        log.info("Fetching bookings for date: {}", date);
        return bookingRepository.findBookingsForDate(date);
    }

    public List<Booking> getCheckInsForDate(LocalDate date) {
        log.info("Fetching check-ins for date: {}", date);
        return bookingRepository.findCheckInsForDate(date);
    }

    public List<Booking> getCheckOutsForDate(LocalDate date) {
        log.info("Fetching check-outs for date: {}", date);
        return bookingRepository.findCheckOutsForDate(date);
    }

    @Transactional
    public Booking createBooking(BookingRequest request) {
        log.info("Creating new booking for guest: {} and room: {}", request.getGuestId(), request.getRoomId());
        
        // Validate dates
        if (request.getCheckOutDate().isBefore(request.getCheckInDate())) {
            throw new ValidationException("Check-out date must be after check-in date");
        }
        
        if (request.getCheckInDate().isBefore(LocalDate.now())) {
            throw new ValidationException("Check-in date cannot be in the past");
        }

        // Get guest and room
        Guest guest = guestService.getGuestById(request.getGuestId());
        Room room = roomService.getRoomById(request.getRoomId());
        
        // Check if room is available
        if (!room.isAvailableForBooking()) {
            throw new ValidationException("Room is not available");
        }
        
        // Check room availability for dates
        List<Room> availableRooms = roomService.findAvailableRooms(request.getCheckInDate(), request.getCheckOutDate());
        boolean isAvailable = availableRooms.stream().anyMatch(r -> r.getId().equals(room.getId()));
        if (!isAvailable) {
            throw new ValidationException("Room is not available for selected dates");
        }

        // Create booking
        Booking booking = new Booking();
        booking.setGuest(guest);
        booking.setRoom(room);
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setNumberOfGuests(request.getNumberOfGuests());
        booking.setBookingStatus("PENDING");
        booking.setPaymentStatus("PENDING");
        booking.setBookingDate(LocalDate.now());
        booking.setSpecialRequests(request.getSpecialRequests());
        
        // Calculate total price
        BigDecimal totalPrice = booking.calculateTotalPrice();
        booking.setTotalPrice(totalPrice);
        
        // Save booking
        Booking savedBooking = bookingRepository.save(booking);
        
        // Mark room as unavailable (temporarily)
        room.setAvailable(false);
        roomService.updateRoom(room.getId(), convertRoomToDTO(room));
        
        // Increment guest stays
        guestService.incrementTotalStays(request.getGuestId());
        
        log.info("Booking created successfully with id: {}", savedBooking.getId());
        return savedBooking;
    }

    @Transactional
    public Booking confirmBooking(Long bookingId) {
        log.info("Confirming booking with id: {}", bookingId);
        Booking booking = getBookingById(bookingId);
        
        if (!"PENDING".equals(booking.getBookingStatus())) {
            throw new ValidationException("Only pending bookings can be confirmed");
        }
        
        booking.setBookingStatus("CONFIRMED");
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking checkIn(Long bookingId) {
        log.info("Checking in booking with id: {}", bookingId);
        Booking booking = getBookingById(bookingId);
        
        if (!"CONFIRMED".equals(booking.getBookingStatus())) {
            throw new ValidationException("Only confirmed bookings can be checked in");
        }
        
        booking.setBookingStatus("CHECKED_IN");
        booking.setCheckedInAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking checkOut(Long bookingId) {
        log.info("Checking out booking with id: {}", bookingId);
        Booking booking = getBookingById(bookingId);
        
        if (!"CHECKED_IN".equals(booking.getBookingStatus())) {
            throw new ValidationException("Only checked-in bookings can be checked out");
        }
        
        booking.setBookingStatus("CHECKED_OUT");
        booking.setCheckedOutAt(LocalDateTime.now());
        
        // Make room available again
        Room room = booking.getRoom();
        room.setAvailable(true);
        roomService.updateRoom(room.getId(), convertRoomToDTO(room));
        
        return bookingRepository.save(booking);
    }

    @Transactional
    public void cancelBooking(Long bookingId) {
        log.info("Cancelling booking with id: {}", bookingId);
        Booking booking = getBookingById(bookingId);
        
        if (!booking.canBeCancelled()) {
            throw new ValidationException("Booking cannot be cancelled");
        }
        
        booking.setBookingStatus("CANCELLED");
        booking.setCancellationReason("Cancelled by user");
        
        // Make room available again
        Room room = booking.getRoom();
        room.setAvailable(true);
        roomService.updateRoom(room.getId(), convertRoomToDTO(room));
        
        bookingRepository.save(booking);
        log.info("Booking cancelled successfully with id: {}", bookingId);
    }

    @Transactional
    public Booking updateBookingStatus(Long bookingId, String status) {
        log.info("Updating booking status: {} -> {}", bookingId, status);
        Booking booking = getBookingById(bookingId);
        booking.setBookingStatus(status);
        return bookingRepository.save(booking);
    }

    @Transactional
    public void processExpiredPendingBookings() {
        LocalDateTime expiryTime = LocalDateTime.now().minusHours(24);
        List<Booking> expiredBookings = bookingRepository.findPendingBookingsOlderThan(expiryTime);
        
        for (Booking booking : expiredBookings) {
            booking.setBookingStatus("EXPIRED");
            Room room = booking.getRoom();
            room.setAvailable(true);
            roomService.updateRoom(room.getId(), convertRoomToDTO(room));
            bookingRepository.save(booking);
            log.info("Expired booking: {}", booking.getId());
        }
    }

    public Long getConfirmedBookingsCount() {
        return bookingRepository.countConfirmedBookings();
    }

    public List<Object[]> getBookingsByDayStats(LocalDate start, LocalDate end) {
        return bookingRepository.countBookingsByDay(start, end);
    }

    public List<Object[]> getBookingStatusStats() {
        return bookingRepository.countBookingsByStatus();
    }

    private com.hotel.hotel_management_system.dto.RoomDTO convertRoomToDTO(Room room) {
        com.hotel.hotel_management_system.dto.RoomDTO dto = new com.hotel.hotel_management_system.dto.RoomDTO();
        dto.setRoomNumber(room.getRoomNumber());
        dto.setRoomType(room.getRoomType());
        dto.setPricePerNight(room.getPricePerNight());
        dto.setCapacity(room.getCapacity());
        dto.setDescription(room.getDescription());
        dto.setAvailable(room.getAvailable());
        dto.setFloorNumber(room.getFloorNumber());
        dto.setHasView(room.getHasView());
        dto.setHasWifi(room.getHasWifi());
        dto.setHasAc(room.getHasAc());
        dto.setHasTv(room.getHasTv());
        dto.setImageUrl(room.getImageUrl());
        return dto;
    }
}