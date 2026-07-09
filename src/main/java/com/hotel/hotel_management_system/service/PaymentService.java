package com.hotel.hotel_management_system.service;

import com.hotel.hotel_management_system.dto.PaymentRequest;
import com.hotel.hotel_management_system.exception.ResourceNotFoundException;
import com.hotel.hotel_management_system.exception.ValidationException;
import com.hotel.hotel_management_system.model.Booking;
import com.hotel.hotel_management_system.model.Payment;
import com.hotel.hotel_management_system.repository.BookingRepository;
import com.hotel.hotel_management_system.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public Payment processPayment(PaymentRequest request) {

        log.info("Processing payment for booking: {}", request.getBookingId());

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Booking not found with id: " + request.getBookingId()));

        // Check if payment already exists
        if (paymentRepository.findByBookingId(request.getBookingId()).isPresent()) {
            throw new ValidationException("Payment already exists for this booking");
        }

        // Validate amount
        if (request.getAmount() == null ||
                request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Payment amount must be greater than 0");
        }

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentStatus("COMPLETED");
        payment.setTransactionId(generateTransactionId());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setCardLastFour(request.getCardLastFour());
        payment.setBillingAddress(request.getBillingAddress());

        // Update booking payment status
        booking.setPaymentStatus("PAID");
        bookingRepository.save(booking);

        Payment savedPayment = paymentRepository.save(payment);

        log.info("Payment processed successfully with id: {}", savedPayment.getId());

        return savedPayment;
    }

    @Transactional
    public Payment refundPayment(Long paymentId) {

        log.info("Refunding payment: {}", paymentId);

        Payment payment = getPaymentById(paymentId);

        if (!"COMPLETED".equals(payment.getPaymentStatus())) {
            throw new ValidationException("Only completed payments can be refunded");
        }

        payment.setPaymentStatus("REFUNDED");

        Booking booking = payment.getBooking();
        booking.setPaymentStatus("REFUNDED");

        bookingRepository.save(booking);

        return paymentRepository.save(payment);
    }

    public Payment getPaymentById(Long id) {

        return paymentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found with id: " + id));
    }

    public Payment getPaymentByBookingId(Long bookingId) {

        return paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found for booking: " + bookingId));
    }

    public List<Payment> getAllPayments() {
        log.info("Fetching all payments");
        return paymentRepository.findAll();
    }

    public List<Payment> getPaymentsByStatus(String status) {
        return paymentRepository.findByPaymentStatus(status);
    }

    public List<Payment> getPaymentsByMethod(String method) {
        return paymentRepository.findByPaymentMethod(method);
    }

    public List<Payment> getPaymentsBetweenDates(LocalDateTime start, LocalDateTime end) {
        return paymentRepository.findPaymentsBetweenDates(start, end);
    }

    public Double getTotalRevenue() {
        Double revenue = paymentRepository.calculateTotalRevenue();
        return revenue != null ? revenue : 0.0;
    }

    public List<Object[]> getDailyRevenue(LocalDateTime start, LocalDateTime end) {
        return paymentRepository.getDailyRevenue(start, end);
    }

    public List<Object[]> getPaymentMethodStats() {
        return paymentRepository.getPaymentMethodStats();
    }

    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}