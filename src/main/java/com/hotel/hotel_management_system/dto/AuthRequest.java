package com.hotel.hotel_management_system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor 
public class AuthRequest {
    
    @NotBlank(message = "Username is required")
    private String Username;

    @NotBlank(message = "Password is required")
    private String password;
}
