package com.hotel.hotel_management_system.controller;

import com.hotel.hotel_management_system.dto.ApiResponse;
import com.hotel.hotel_management_system.dto.RoomDTO;
import com.hotel.hotel_management_system.model.Room;
import com.hotel.hotel_management_system.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<List<Room>>> getAllRooms() {
        List<Room> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(ApiResponse.success("Rooms retrieved successfully", rooms));
    }

    @GetMapping("/available")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<List<Room>>> getAvailableRooms() {
        List<Room> rooms = roomService.getAvailableRooms();
        return ResponseEntity.ok(ApiResponse.success("Available rooms retrieved successfully", rooms));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<Room>> getRoomById(@PathVariable Long id) {
        Room room = roomService.getRoomById(id);
        return ResponseEntity.ok(ApiResponse.success("Room retrieved successfully", room));
    }

    @GetMapping("/search")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<List<Room>>> searchAvailableRooms(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam(required = false) String roomType) {
        
        List<Room> rooms;
        if (roomType != null) {
            rooms = roomService.findAvailableRoomsByTypeAndDates(roomType, checkIn, checkOut);
        } else {
            rooms = roomService.findAvailableRooms(checkIn, checkOut);
        }
        
        return ResponseEntity.ok(ApiResponse.success("Available rooms retrieved successfully", rooms));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Room>> createRoom(@Valid @RequestBody RoomDTO roomDTO) {
        Room room = roomService.createRoom(roomDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Room created successfully", room));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Room>> updateRoom(
            @PathVariable Long id, 
            @Valid @RequestBody RoomDTO roomDTO) {
        Room room = roomService.updateRoom(id, roomDTO);
        return ResponseEntity.ok(ApiResponse.success("Room updated successfully", room));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok(ApiResponse.success("Room deleted successfully", null));
    }
}