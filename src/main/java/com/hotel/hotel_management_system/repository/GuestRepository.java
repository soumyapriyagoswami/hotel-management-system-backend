package com.hotel.hotel_management_system.repository;

import com.hotel.hotel_management_system.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

    Optional<Guest> findByEmail(String email);

    Optional<Guest> findByEmailAndPhoneNumber(String email, String phoneNumber);

    List<Guest> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);

    List<Guest> findByIsVipTrue();

    @Query("SELECT g FROM Guest g WHERE g.email = :email OR g.phoneNumber = :phone")
    Optional<Guest> findByEmailOrPhoneNumber(@Param("email") String email, 
                                            @Param("phone") String phone);

    @Query("SELECT g FROM Guest g JOIN g.bookings b WHERE b.bookingStatus = 'CONFIRMED' AND b.checkInDate <= CURRENT_DATE AND b.checkOutDate >= CURRENT_DATE")
    List<Guest> findCurrentGuests();

    @Query("SELECT COUNT(g) FROM Guest g")
    Long countTotalGuests();

    @Query("SELECT g.city, COUNT(g) FROM Guest g GROUP BY g.city")
    List<Object[]> countGuestsByCity();
}