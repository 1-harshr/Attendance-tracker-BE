package com.example.attendance_tracker.attendace_tracker.dto.employee;

public class UpdateEmployeeResponse {
    
    private String message;
    private String employeeId;
    
    public UpdateEmployeeResponse() {}
    
    public UpdateEmployeeResponse(String message, String employeeId) {
        this.message = message;
        this.employeeId = employeeId;
    }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
} 