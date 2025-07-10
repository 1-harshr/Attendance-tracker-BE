package com.example.attendance_tracker.attendace_tracker.dto.auth;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    
    @NotBlank(message = "Employee ID is required")
    private String employeeId;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
} 