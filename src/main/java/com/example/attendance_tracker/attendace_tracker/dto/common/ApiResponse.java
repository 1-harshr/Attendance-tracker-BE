package com.example.attendance_tracker.attendace_tracker.dto.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiResponse<T> {
    
    private boolean success;
    private T data;
    private ErrorInfo error;
    
    public ApiResponse() {}
    
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.data = data;
        return response;
    }
    
    public static <T> ApiResponse<T> error(String code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.error = new ErrorInfo(code, message);
        return response;
    }

    @Setter
    @Getter
    public static class ErrorInfo {
        private String code;
        private String message;
        
        public ErrorInfo() {}
        
        public ErrorInfo(String code, String message) {
            this.code = code;
            this.message = message;
        }

    }
} 