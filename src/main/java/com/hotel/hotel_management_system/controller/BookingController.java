package com.hotel.hotel_management_system.controller;

import com.hotel.hotel_management_system.dto.ApiResponse;
import com.hotel.hotel_management_system.dto.BookingRequest;
import com.hotel.hotel_management_system.model.Booking;
import com.hotel.hotel_management_system.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Booking>>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(ApiResponse.success("Bookings retrieved successfully", bookings));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Booking>> getBookingById(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(ApiResponse.success("Booking retrieved successfully", booking));
    }

    @GetMapping("/guest/{guestId}")
    public ResponseEntity<ApiResponse<List<Booking>>> getBookingsByGuest(@PathVariable Long guestId) {
        List<Booking> bookings = bookingService.getBookingsByGuest(guestId);
        return ResponseEntity.ok(ApiResponse.success("Guest bookings retrieved successfully", bookings));
    }

    @GetMapping("/guest/{guestId}/active")
    public ResponseEntity<ApiResponse<List<Booking>>> getActiveBookingsByGuest(@PathVariable Long guestId) {
        List<Booking> bookings = bookingService.getActiveBookingsByGuest(guestId);
        return ResponseEntity.ok(ApiResponse.success("Active guest bookings retrieved successfully", bookings));
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<ApiResponse<List<Booking>>> getBookingsByRoom(@PathVariable Long roomId) {
        List<Booking> bookings = bookingService.getBookingsByRoom(roomId);
        return ResponseEntity.ok(ApiResponse.success("Room bookings retrieved successfully", bookings));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<Booking>>> getBookingsByStatus(@PathVariable String status) {
        List<Booking> bookings = bookingService.getBookingsByStatus(status.toUpperCase());
        return ResponseEntity.ok(ApiResponse.success("Bookings by status retrieved successfully", bookings));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<ApiResponse<List<Booking>>> getBookingsForDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Booking> bookings = bookingService.getBookingsForDate(date);
        return ResponseEntity.ok(ApiResponse.success("Bookings for date retrieved successfully", bookings));
    }

    @GetMapping("/checkins/{date}")
    public ResponseEntity<ApiResponse<List<Booking>>> getCheckInsForDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Booking> bookings = bookingService.getCheckInsForDate(date);
        return ResponseEntity.ok(ApiResponse.success("Check-ins for date retrieved successfully", bookings));
    }

    @GetMapping("/checkouts/{date}")
    public ResponseEntity<ApiResponse<List<Booking>>> getCheckOutsForDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Booking> bookings = bookingService.getCheckOutsForDate(date);
        return ResponseEntity.ok(ApiResponse.success("Check-outs for date retrieved successfully", bookings));
    }

    @GetMapping("/stats/status")
    public ResponseEntity<ApiResponse<Object>> getBookingStatusStats() {
        return ResponseEntity.ok(ApiResponse.success("Booking status statistics retrieved", 
                bookingService.getBookingStatusStats()));
    }

    @GetMapping("/stats/daily")
    public ResponseEntity<ApiResponse<Object>> getDailyBookingStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(ApiResponse.success("Daily booking statistics retrieved", 
                bookingService.getBookingsByDayStats(start, end)));
    }

    @GetMapping("/count/confirmed")
    public ResponseEntity<ApiResponse<Long>> getConfirmedBookingsCount() {
        Long count = bookingService.getConfirmedBookingsCount();
        return ResponseEntity.ok(ApiResponse.success("Confirmed bookings count retrieved", count));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Booking>> createBooking(@Valid @RequestBody BookingRequest request) {
        Booking booking = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Booking created successfully", booking));
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse<Booking>> confirmBooking(@PathVariable Long id) {
        Booking booking = bookingService.confirmBooking(id);
        return ResponseEntity.ok(ApiResponse.success("Booking confirmed successfully", booking));
    }

    @PatchMapping("/{id}/checkin")
    public ResponseEntity<ApiResponse<Booking>> checkIn(@PathVariable Long id) {
        Booking booking = bookingService.checkIn(id);
        return ResponseEntity.ok(ApiResponse.success("Check-in completed successfully", booking));
    }

    @PatchMapping("/{id}/checkout")
    public ResponseEntity<ApiResponse<Booking>> checkOut(@PathVariable Long id) {
        Booking booking = bookingService.checkOut(id);
        return ResponseEntity.ok(ApiResponse.success("Check-out completed successfully", booking));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Booking>> updateBookingStatus(
            @PathVariable Long id, 
            @RequestParam String status) {
        Booking booking = bookingService.updateBookingStatus(id, status.toUpperCase());
        return ResponseEntity.ok(ApiResponse.success("Booking status updated successfully", booking));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok(ApiResponse.success("Booking cancelled successfully", null));
    }
}