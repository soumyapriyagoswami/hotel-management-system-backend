package com.hotel.hotel_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long id;
    private Long guestId;
    private String guestName;
    private Long roomId;
    private String roomNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer numberOfGuests;
    private String bookingStatus;
    private String paymentStatus;
    private Double totalPrice;
    private LocalDateTime createdAt;
}