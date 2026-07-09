package com.hotel.hotel_management_system.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    
    @NotNull(message = "Guest ID is required")
    private Long guestId;
    
    @NotNull(message = "Room ID is required")
    private Long roomId;
    
    @NotNull(message = "Check-in date is required")
    @FutureOrPresent(message = "Check-in date must be today or in the future")
    private LocalDate checkInDate;
    
    @NotNull(message = "Check-out date is required")
    private LocalDate checkOutDate;
    
    @NotNull(message = "Number of guests is required")
    @Min(value = 1, message = "Must have at least 1 guest")
    @Max(value = 10, message = "Maximum 10 guests allowed")
    private Integer numberOfGuests;
    
    @Size(max = 1000, message = "Special requests cannot exceed 1000 characters")
    private String specialRequests;
}