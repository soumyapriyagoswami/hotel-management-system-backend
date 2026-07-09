package com.hotel.hotel_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timeStamp;

    public ApiResponse(boolean success, String message, T data){
        this.success = success;
        this.message = message;
        this.data = data;
        this.timeStamp = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse<>(true, "success", data);
    }

    public static <T> ApiResponse<T> success(String message, T data){
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> error(String message){
        return new ApiResponse<>(false, message, null);
    }
}
