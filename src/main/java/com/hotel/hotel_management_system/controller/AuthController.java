package com.hotel.hotel_management_system.controller;

import com.hotel.hotel_management_system.dto.ApiResponse;
import com.hotel.hotel_management_system.dto.AuthRequest;
import com.hotel.hotel_management_system.dto.AuthResponse;
import com.hotel.hotel_management_system.dto.RegisterRequest;
import com.hotel.hotel_management_system.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Registration successful", response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String refreshToken = token.substring(7);
            AuthResponse response = authService.refreshToken(refreshToken);
            return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", response));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error("Invalid refresh token"));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            // Extract username from token and logout
            // Implementation depends on your token storage strategy
            return ResponseEntity.ok(ApiResponse.success("Logout successful", null));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error("Invalid token"));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @RequestParam String username,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        authService.changePassword(username, oldPassword, newPassword);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully", null));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestParam String email) {
        authService.resetPassword(email);
        return ResponseEntity.ok(ApiResponse.success("Password reset email sent successfully", null));
    }

    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<Boolean>> validateToken(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            // Token validation is handled by JwtAuthenticationFilter
            return ResponseEntity.ok(ApiResponse.success("Token is valid", true));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error("Invalid token format"));
    }
}