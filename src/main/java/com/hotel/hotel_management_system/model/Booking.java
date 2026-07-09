package com.hotel.hotel_management_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "BOOKINGS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_seq")
    @SequenceGenerator(name = "booking_seq", sequenceName = "BOOKING_SEQ", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Guest is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "guest_id", nullable = false)
    private Guest guest;

    @NotNull(message = "Room is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @NotNull(message = "Check-in date is required")
    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    @NotNull(message = "Check-out date is required")
    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;

    @NotNull(message = "Number of guests is required")
    @Min(value = 1)
    @Max(value = 10)
    @Column(name = "number_of_guests", nullable = false)
    private Integer numberOfGuests;

    @Column(name = "booking_status", nullable = false, length = 20)
    private String bookingStatus = "PENDING";

    @Column(name = "payment_status", length = 20)
    private String paymentStatus = "PENDING";

    @Column(name = "booking_date")
    private LocalDate bookingDate;

    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "special_requests", length = 1000)
    private String specialRequests;

    @Column(name = "checked_in_at")
    private LocalDateTime checkedInAt;

    @Column(name = "checked_out_at")
    private LocalDateTime checkedOutAt;

    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Payment payment;

    // Calculate total nights
    public long calculateNights() {
        return java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    // Calculate total price
    public BigDecimal calculateTotalPrice() {
    if (room != null && room.getPricePerNight() != null) {
        return room.getPricePerNight()
                .multiply(BigDecimal.valueOf(calculateNights()));
    }
    return BigDecimal.ZERO;
}

    // Check if booking is active
    public boolean isActive() {
        return "PENDING".equals(bookingStatus) || "CONFIRMED".equals(bookingStatus);
    }

    // Check if booking can be cancelled
    public boolean canBeCancelled() {
        return isActive() && LocalDate.now().isBefore(checkInDate);
    }
}