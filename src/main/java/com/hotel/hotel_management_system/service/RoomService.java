package com.hotel.hotel_management_system.service;

import com.hotel.hotel_management_system.dto.RoomDTO;
import com.hotel.hotel_management_system.exception.ResourceNotFoundException;
import com.hotel.hotel_management_system.model.Room;
import com.hotel.hotel_management_system.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {

    private final RoomRepository roomRepository;

    public List<Room> getAllRooms() {
        log.info("Fetching all rooms");
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        log.info("Fetching room with id: {}", id);
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
    }

    @Transactional
    public Room createRoom(RoomDTO roomDTO) {
        log.info("Creating new room: {}", roomDTO.getRoomNumber());
        
        Room room = new Room();
        room.setRoomNumber(roomDTO.getRoomNumber());
        room.setRoomType(roomDTO.getRoomType());
        room.setPricePerNight(roomDTO.getPricePerNight());
        room.setCapacity(roomDTO.getCapacity());
        room.setDescription(roomDTO.getDescription());
        room.setAvailable(roomDTO.getAvailable() != null ? roomDTO.getAvailable() : true);
        room.setFloorNumber(roomDTO.getFloorNumber());
        room.setHasView(roomDTO.getHasView() != null && roomDTO.getHasView());
        room.setHasWifi(roomDTO.getHasWifi() != null && roomDTO.getHasWifi());
        room.setHasAc(roomDTO.getHasAc() != null && roomDTO.getHasAc());
        room.setHasTv(roomDTO.getHasTv() != null && roomDTO.getHasTv());
        room.setImageUrl(roomDTO.getImageUrl());
        
        return roomRepository.save(room);
    }

    @Transactional
    public Room updateRoom(Long id, RoomDTO roomDTO) {
        log.info("Updating room with id: {}", id);
        Room room = getRoomById(id);
        
        room.setRoomNumber(roomDTO.getRoomNumber());
        room.setRoomType(roomDTO.getRoomType());
        room.setPricePerNight(roomDTO.getPricePerNight());
        room.setCapacity(roomDTO.getCapacity());
        room.setDescription(roomDTO.getDescription());
        room.setAvailable(roomDTO.getAvailable());
        room.setFloorNumber(roomDTO.getFloorNumber());
        room.setHasView(roomDTO.getHasView());
        room.setHasWifi(roomDTO.getHasWifi());
        room.setHasAc(roomDTO.getHasAc());
        room.setHasTv(roomDTO.getHasTv());
        room.setImageUrl(roomDTO.getImageUrl());
        
        return roomRepository.save(room);
    }

    @Transactional
    public void deleteRoom(Long id) {
        log.info("Deleting room with id: {}", id);
        Room room = getRoomById(id);
        roomRepository.delete(room);
    }

    public List<Room> getAvailableRooms() {
        log.info("Fetching available rooms");
        return roomRepository.findByAvailableTrue();
    }

    public List<Room> getRoomsByType(String roomType) {
        log.info("Fetching rooms by type: {}", roomType);
        return roomRepository.findByRoomType(roomType);
    }

    public List<Room> getAvailableRoomsByType(String roomType) {
        log.info("Fetching available rooms by type: {}", roomType);
        return roomRepository.findByRoomTypeAndAvailableTrue(roomType);
    }

    public List<Room> findAvailableRooms(LocalDate checkIn, LocalDate checkOut) {
        log.info("Finding available rooms between {} and {}", checkIn, checkOut);
        return roomRepository.findAvailableRoomsBetweenDates(checkIn, checkOut);
    }

    public List<Room> findAvailableRoomsByTypeAndDates(String roomType, LocalDate checkIn, LocalDate checkOut) {
        log.info("Finding available rooms by type {} between {} and {}", roomType, checkIn, checkOut);
        return roomRepository.findAvailableRoomsByTypeAndDates(roomType, checkIn, checkOut);
    }

    public List<Room> findAvailableRoomsByPriceAndCapacity(BigDecimal minPrice, BigDecimal maxPrice, Integer capacity) {
        log.info("Finding available rooms by price range {} - {} and capacity {}", minPrice, maxPrice, capacity);
        return roomRepository.findAvailableRoomsByPriceAndCapacity(minPrice, maxPrice, capacity);
    }

    public Long getAvailableRoomsCount() {
        return roomRepository.countAvailableRooms();
    }

    public List<Object[]> getRoomTypeStats() {
        return roomRepository.countRoomsByType();
    }
}