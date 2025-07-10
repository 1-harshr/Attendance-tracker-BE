package com.example.attendance_tracker.attendace_tracker.dto.attendance;

public class CheckOutResponse {
    
    private String message;
    private Long attendanceId;
    private Long checkOutTime;
    private Double distanceFromOffice;
    private String status;
    
    public CheckOutResponse() {}
    
    public CheckOutResponse(String message, Long attendanceId, Long checkOutTime, Double distanceFromOffice, String status) {
        this.message = message;
        this.attendanceId = attendanceId;
        this.checkOutTime = checkOutTime;
        this.distanceFromOffice = distanceFromOffice;
        this.status = status;
    }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public Long getAttendanceId() { return attendanceId; }
    public void setAttendanceId(Long attendanceId) { this.attendanceId = attendanceId; }
    
    public Long getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(Long checkOutTime) { this.checkOutTime = checkOutTime; }
    
    public Double getDistanceFromOffice() { return distanceFromOffice; }
    public void setDistanceFromOffice(Double distanceFromOffice) { this.distanceFromOffice = distanceFromOffice; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
} 