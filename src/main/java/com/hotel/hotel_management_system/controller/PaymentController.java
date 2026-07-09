package com.hotel.hotel_management_system.controller;

import com.hotel.hotel_management_system.dto.ApiResponse;
import com.hotel.hotel_management_system.dto.PaymentRequest;
import com.hotel.hotel_management_system.model.Payment;
import com.hotel.hotel_management_system.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Payment>>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(ApiResponse.success("Payments retrieved successfully", payments));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Payment>> getPaymentById(@PathVariable Long id) {
        Payment payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(ApiResponse.success("Payment retrieved successfully", payment));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<ApiResponse<Payment>> getPaymentByBookingId(@PathVariable Long bookingId) {
        Payment payment = paymentService.getPaymentByBookingId(bookingId);
        return ResponseEntity.ok(ApiResponse.success("Payment retrieved successfully", payment));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<Payment>>> getPaymentsByStatus(@PathVariable String status) {
        List<Payment> payments = paymentService.getPaymentsByStatus(status.toUpperCase());
        return ResponseEntity.ok(ApiResponse.success("Payments by status retrieved successfully", payments));
    }

    @GetMapping("/method/{method}")
    public ResponseEntity<ApiResponse<List<Payment>>> getPaymentsByMethod(@PathVariable String method) {
        List<Payment> payments = paymentService.getPaymentsByMethod(method.toUpperCase());
        return ResponseEntity.ok(ApiResponse.success("Payments by method retrieved successfully", payments));
    }

    @GetMapping("/date-range")
    public ResponseEntity<ApiResponse<List<Payment>>> getPaymentsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<Payment> payments = paymentService.getPaymentsBetweenDates(start, end);
        return ResponseEntity.ok(ApiResponse.success("Payments between dates retrieved successfully", payments));
    }

    @GetMapping("/revenue/total")
    public ResponseEntity<ApiResponse<Double>> getTotalRevenue() {
        Double revenue = paymentService.getTotalRevenue();
        return ResponseEntity.ok(ApiResponse.success("Total revenue retrieved successfully", revenue));
    }

    @GetMapping("/revenue/daily")
    public ResponseEntity<ApiResponse<Object>> getDailyRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(ApiResponse.success("Daily revenue retrieved successfully", 
                paymentService.getDailyRevenue(start, end)));
    }

    @GetMapping("/stats/methods")
    public ResponseEntity<ApiResponse<Object>> getPaymentMethodStats() {
        return ResponseEntity.ok(ApiResponse.success("Payment method statistics retrieved", 
                paymentService.getPaymentMethodStats()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Payment>> processPayment(@Valid @RequestBody PaymentRequest request) {
        Payment payment = paymentService.processPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Payment processed successfully", payment));
    }

    @PatchMapping("/{id}/refund")
    public ResponseEntity<ApiResponse<Payment>> refundPayment(@PathVariable Long id) {
        Payment payment = paymentService.refundPayment(id);
        return ResponseEntity.ok(ApiResponse.success("Payment refunded successfully", payment));
    }
}