package com.hotel.hotel_management_system.repository;

import com.hotel.hotel_management_system.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByGuestId(Long guestId);

    List<Booking> findByRoomId(Long roomId);

    List<Booking> findByBookingStatus(String status);

    List<Booking> findByCheckInDateBetween(LocalDate startDate, LocalDate endDate);

    List<Booking> findByCheckOutDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT b FROM Booking b WHERE b.guest.id = :guestId AND b.bookingStatus IN ('PENDING', 'CONFIRMED', 'CHECKED_IN')")
    List<Booking> findActiveBookingsByGuestId(@Param("guestId") Long guestId);

    @Query("SELECT b FROM Booking b WHERE b.room.id = :roomId AND b.bookingStatus IN ('PENDING', 'CONFIRMED', 'CHECKED_IN')")
    List<Booking> findActiveBookingsByRoomId(@Param("roomId") Long roomId);

    @Query("SELECT b FROM Booking b WHERE b.bookingStatus = 'CONFIRMED' AND b.checkInDate <= :date AND b.checkOutDate >= :date")
    List<Booking> findBookingsForDate(@Param("date") LocalDate date);

    @Query("SELECT b FROM Booking b WHERE b.bookingStatus = 'CONFIRMED' AND b.checkInDate = :date")
    List<Booking> findCheckInsForDate(@Param("date") LocalDate date);

    @Query("SELECT b FROM Booking b WHERE b.bookingStatus = 'CONFIRMED' AND b.checkOutDate = :date")
    List<Booking> findCheckOutsForDate(@Param("date") LocalDate date);

    @Query("SELECT b FROM Booking b WHERE b.bookingStatus = 'PENDING' AND b.createdAt <= :expiryTime")
    List<Booking> findPendingBookingsOlderThan(@Param("expiryTime") LocalDateTime expiryTime);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.bookingStatus = 'CONFIRMED'")
    Long countConfirmedBookings();

    @Query("SELECT DATE(b.checkInDate), COUNT(b) FROM Booking b " +
           "WHERE b.bookingStatus = 'CONFIRMED' AND b.checkInDate BETWEEN :start AND :end " +
           "GROUP BY DATE(b.checkInDate)")
    List<Object[]> countBookingsByDay(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT b.bookingStatus, COUNT(b) FROM Booking b GROUP BY b.bookingStatus")
    List<Object[]> countBookingsByStatus();
}