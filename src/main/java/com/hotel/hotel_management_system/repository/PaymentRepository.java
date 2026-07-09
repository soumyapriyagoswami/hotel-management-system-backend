package com.hotel.hotel_management_system.repository;

import com.hotel.hotel_management_system.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByBookingId(Long bookingId);

    List<Payment> findByPaymentStatus(String status);

    List<Payment> findByPaymentMethod(String paymentMethod);

    @Query("SELECT p FROM Payment p WHERE p.paymentDate BETWEEN :start AND :end")
    List<Payment> findPaymentsBetweenDates(@Param("start") LocalDateTime start, 
                                          @Param("end") LocalDateTime end);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentStatus = 'COMPLETED'")
    Double calculateTotalRevenue();

    @Query("SELECT DATE(p.paymentDate), SUM(p.amount) FROM Payment p " +
           "WHERE p.paymentStatus = 'COMPLETED' AND p.paymentDate BETWEEN :start AND :end " +
           "GROUP BY DATE(p.paymentDate)")
    List<Object[]> getDailyRevenue(@Param("start") LocalDateTime start, 
                                  @Param("end") LocalDateTime end);

    @Query("SELECT p.paymentMethod, COUNT(p), SUM(p.amount) FROM Payment p " +
           "WHERE p.paymentStatus = 'COMPLETED' GROUP BY p.paymentMethod")
    List<Object[]> getPaymentMethodStats();
}