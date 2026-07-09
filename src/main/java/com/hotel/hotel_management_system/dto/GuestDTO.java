package com.hotel.hotel_management_system.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuestDTO {
    
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;
    
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be 10-15 digits with optional + prefix")
    private String phoneNumber;
    
    @Size(max = 500, message = "Address cannot exceed 500 characters")
    private String address;
    
    @Size(max = 100, message = "City cannot exceed 100 characters")
    private String city;
    
    @Size(max = 100, message = "Country cannot exceed 100 characters")
    private String country;
    
    @Size(max = 20, message = "Postal code cannot exceed 20 characters")
    private String postalCode;
    
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
    
    @Size(max = 50, message = "ID card number cannot exceed 50 characters")
    private String idCardNumber;
    
    @Size(max = 100, message = "Emergency contact name cannot exceed 100 characters")
    private String emergencyContactName;
    
    @Size(max = 20, message = "Emergency contact phone cannot exceed 20 characters")
    private String emergencyContactPhone;
    
    @Size(max = 1000, message = "Special requirements cannot exceed 1000 characters")
    private String specialRequirements;
    
    private Boolean isVip = false;
}