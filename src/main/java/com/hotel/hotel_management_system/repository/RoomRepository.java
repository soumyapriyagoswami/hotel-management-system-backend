package com.hotel.hotel_management_system.repository;

import com.hotel.hotel_management_system.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByAvailableTrue();

    List<Room> findByRoomTypeAndAvailableTrue(String roomType);

    List<Room> findByRoomType(String roomType);

    List<Room> findByPricePerNightLessThanEqual(BigDecimal maxPrice);

    List<Room> findByCapacityGreaterThanEqual(Integer capacity);

    @Query("SELECT r FROM Room r WHERE r.available = true AND r.id NOT IN " +
           "(SELECT b.room.id FROM Booking b WHERE b.bookingStatus IN ('PENDING', 'CONFIRMED', 'CHECKED_IN') " +
           "AND ((b.checkInDate <= :checkOut AND b.checkOutDate >= :checkIn)))")
    List<Room> findAvailableRoomsBetweenDates(@Param("checkIn") LocalDate checkIn, 
                                             @Param("checkOut") LocalDate checkOut);

    @Query("SELECT r FROM Room r WHERE r.available = true AND r.roomType = :roomType AND r.id NOT IN " +
           "(SELECT b.room.id FROM Booking b WHERE b.bookingStatus IN ('PENDING', 'CONFIRMED', 'CHECKED_IN') " +
           "AND ((b.checkInDate <= :checkOut AND b.checkOutDate >= :checkIn)))")
    List<Room> findAvailableRoomsByTypeAndDates(@Param("roomType") String roomType,
                                               @Param("checkIn") LocalDate checkIn,
                                               @Param("checkOut") LocalDate checkOut);

    @Query("SELECT r FROM Room r WHERE r.available = true " +
           "AND r.pricePerNight BETWEEN :minPrice AND :maxPrice " +
           "AND r.capacity >= :capacity")
    List<Room> findAvailableRoomsByPriceAndCapacity(@Param("minPrice") BigDecimal minPrice,
                                                    @Param("maxPrice") BigDecimal maxPrice,
                                                    @Param("capacity") Integer capacity);

    @Query("SELECT COUNT(r) FROM Room r WHERE r.available = true")
    Long countAvailableRooms();

    @Query("SELECT r.roomType, COUNT(r) FROM Room r GROUP BY r.roomType")
    List<Object[]> countRoomsByType();
}