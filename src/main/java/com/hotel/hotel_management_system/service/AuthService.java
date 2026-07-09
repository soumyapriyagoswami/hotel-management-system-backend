package com.hotel.hotel_management_system.service;

import com.hotel.hotel_management_system.dto.AuthRequest;
import com.hotel.hotel_management_system.dto.AuthResponse;
import com.hotel.hotel_management_system.dto.RegisterRequest;
import com.hotel.hotel_management_system.exception.ResourceNotFoundException;
import com.hotel.hotel_management_system.exception.ValidationException;
import com.hotel.hotel_management_system.model.User;
import com.hotel.hotel_management_system.repository.UserRepository;
import com.hotel.hotel_management_system.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Transactional
    public AuthResponse login(AuthRequest request) {
        log.info("Login attempt for user: {}", request.getUsername());
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
                )
            );
            
            User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            
            String accessToken = tokenProvider.generateToken(authentication);
            String refreshToken = tokenProvider.generateRefreshToken(authentication);
            
            return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(tokenProvider.getExpirationDateFromToken(accessToken).getTime() / 1000)
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
                
        } catch (BadCredentialsException e) {
            log.error("Invalid credentials for user: {}", request.getUsername());
            throw new ValidationException("Invalid username or password");
        } catch (Exception e) {
            log.error("Login failed for user: {}", request.getUsername(), e);
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registration attempt for user: {}", request.getUsername());
        
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ValidationException("Username already exists");
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ValidationException("Email already registered");
        }
        
        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        
        // Set default role if none provided
        Set<String> roles = request.getRoles();
        if (roles == null || roles.isEmpty()) {
            roles = new HashSet<>();
            roles.add("RECEPTIONIST");
        }
        user.setRoles(roles);
        
        user.setEnabled(true);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with id: {}", savedUser.getId());
        
        // Auto login after registration
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername(request.getUsername());
        authRequest.setPassword(request.getPassword());
        
        return login(authRequest);
    }

    @Transactional
    public AuthResponse refreshToken(String refreshToken) {
        log.info("Refreshing token");
        
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new ValidationException("Invalid refresh token");
        }
        
        String username = tokenProvider.getUsernameFromToken(refreshToken);
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Generate new tokens
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            user, null, user.getAuthorities()
        );
        
        String newAccessToken = tokenProvider.generateToken(authentication);
        String newRefreshToken = tokenProvider.generateRefreshToken(authentication);
        
        return AuthResponse.builder()
            .accessToken(newAccessToken)
            .refreshToken(newRefreshToken)
            .tokenType("Bearer")
            .expiresIn(tokenProvider.getExpirationDateFromToken(newAccessToken).getTime() / 1000)
            .username(user.getUsername())
            .email(user.getEmail())
            .roles(user.getRoles())
            .build();
    }

    @Transactional
    public void logout(String username) {
        log.info("Logout for user: {}", username);
        // Invalidate token on server side (if using token blacklist)
        // For JWT, client-side removal is sufficient
    }

    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        log.info("Password change for user: {}", username);
        
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new ValidationException("Current password is incorrect");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        log.info("Password changed successfully for user: {}", username);
    }

    @Transactional
    public void resetPassword(String email) {
        log.info("Password reset requested for email: {}", email);
        
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        
        // Generate temporary password and send email (implement email service)
        String tempPassword = generateTempPassword();
        user.setPassword(passwordEncoder.encode(tempPassword));
        userRepository.save(user);
        
        // Send email with temporary password
        sendPasswordResetEmail(user, tempPassword);
        
        log.info("Password reset completed for user: {}", user.getUsername());
    }

    private String generateTempPassword() {
        // Generate a random 8-character password
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    private void sendPasswordResetEmail(User user, String tempPassword) {
        // Implement email sending logic
        log.info("Sending password reset email to: {} with temp password: {}", user.getEmail(), tempPassword);
        // Use JavaMailSender or third-party email service
    }
}