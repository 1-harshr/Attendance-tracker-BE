package com.example.attendance_tracker.attendace_tracker.dto.attendance;

public class UpdateAttendanceRequest {
    
    private Long checkInTime;
    private Long checkOutTime;
    private String status;
    
    public UpdateAttendanceRequest() {}
    
    public UpdateAttendanceRequest(Long checkInTime, Long checkOutTime, String status) {
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.status = status;
    }
    
    public Long getCheckInTime() { return checkInTime; }
    public void setCheckInTime(Long checkInTime) { this.checkInTime = checkInTime; }
    
    public Long getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(Long checkOutTime) { this.checkOutTime = checkOutTime; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
} 