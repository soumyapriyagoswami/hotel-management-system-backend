package com.hotel.hotel_management_system.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    
    @NotBlank(message = "Room number is required")
    private String roomNumber;
    
    @NotBlank(message = "Room type is required")
    private String roomType;
    
    @NotNull(message = "Price per night is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @DecimalMax(value = "9999.99", message = "Price cannot exceed 9999.99")
    private BigDecimal pricePerNight;
    
    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    @Max(value = 10, message = "Capacity cannot exceed 10")
    private Integer capacity;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    
    private Boolean available = true;
    
    @Min(value = 0, message = "Floor number must be 0 or greater")
    private Integer floorNumber;
    
    private Boolean hasView = false;
    private Boolean hasWifi = true;
    private Boolean hasAc = true;
    private Boolean hasTv = true;
    
    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    private String imageUrl;
}