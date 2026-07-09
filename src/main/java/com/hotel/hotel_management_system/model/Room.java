package com.hotel.hotel_management_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
    name = "ROOMS",
    uniqueConstraints = @UniqueConstraint(columnNames = "room_number")
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "room_seq"
    )
    @SequenceGenerator(
        name = "room_seq",
        sequenceName = "ROOM_SEQ",
        allocationSize = 1
    )
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Room Number is required")
    @Column(name = "room_number", nullable = false, length = 20)
    private String roomNumber;

    @NotBlank(message = "Room Type is required")
    @Column(name = "room_type", nullable = false, length = 50)
    private String roomType;

    @NotNull(message = "Price per night is required")
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(
        name = "price_per_night",
        nullable = false,
        precision = 10,
        scale = 2
    )
    private BigDecimal pricePerNight;

    @NotNull(message = "Capacity is required")
    @Min(1)
    @Max(10)
    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "available", nullable = false)
    private Boolean available = true;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "floor_number")
    private Integer floorNumber;

    @Column(name = "has_view")
    private Boolean hasView = false;

    @Column(name = "has_wifi")
    private Boolean hasWifi = true;

    @Column(name = "has_ac")
    private Boolean hasAc = true;

    @Column(name = "has_tv")
    private Boolean hasTv = true;

    @Column(name = "image_url")
    private String imageUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(
        mappedBy = "room",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY
    )
    private List<Booking> bookings;

    public boolean isAvailableForBooking() {
        return Boolean.TRUE.equals(available);
    }
}