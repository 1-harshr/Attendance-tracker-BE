package com.example.attendance_tracker.attendace_tracker.dto.common;

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
    
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
    
    public ErrorInfo getError() { return error; }
    public void setError(ErrorInfo error) { this.error = error; }
    
    public static class ErrorInfo {
        private String code;
        private String message;
        
        public ErrorInfo() {}
        
        public ErrorInfo(String code, String message) {
            this.code = code;
            this.message = message;
        }
        
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
} 