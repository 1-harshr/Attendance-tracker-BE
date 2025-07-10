package com.example.attendance_tracker.attendace_tracker.dto.attendance;

import jakarta.validation.constraints.NotNull;

public class ManualAttendanceRequest {
    
    @NotNull(message = "Employee ID is required")
    private String employeeId;
    
    @NotNull(message = "Check-in time is required")
    private Long checkInTime;
    
    private Long checkOutTime;
    
    @NotNull(message = "Status is required")
    private String status;
    
    public ManualAttendanceRequest() {}
    
    public ManualAttendanceRequest(String employeeId, Long checkInTime, Long checkOutTime, String status) {
        this.employeeId = employeeId;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.status = status;
    }
    
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    
    public Long getCheckInTime() { return checkInTime; }
    public void setCheckInTime(Long checkInTime) { this.checkInTime = checkInTime; }
    
    public Long getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(Long checkOutTime) { this.checkOutTime = checkOutTime; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
} 