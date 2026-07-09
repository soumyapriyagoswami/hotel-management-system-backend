package com.hotel.hotel_management_system.controller;

import com.hotel.hotel_management_system.dto.ApiResponse;
import com.hotel.hotel_management_system.service.BookingService;
import com.hotel.hotel_management_system.service.GuestService;
import com.hotel.hotel_management_system.service.PaymentService;
import com.hotel.hotel_management_system.service.RoomService;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashBoardController {
    
    private final RoomService roomService;
    private final GuestService guestService;
    private final BookingService bookingService;
    private final PaymentService paymentService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashBoardStats(){
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalRooms", roomService.getAllRooms().size());
        stats.put("availableRooms", roomService.getAvailableRoomsCount());
        stats.put("totalGuests", guestService.getTotalGuestsCount());
        stats.put("currentGuests", guestService.getCurrentGuests().size());
        stats.put("totalBookings", bookingService.getAllBookings().size());
        stats.put("confirmedBookings", bookingService.getConfirmedBookingsCount());
        stats.put("totalRevenue", paymentService.getTotalRevenue());

        return ResponseEntity.ok(ApiResponse.success("Dashboard statictics retrieved successfully", stats));

    }
}
