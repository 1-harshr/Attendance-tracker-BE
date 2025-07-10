package com.example.attendance_tracker.attendace_tracker.dto.attendance;

public class CheckInResponse {
    
    private String message;
    private Long attendanceId;
    private Long checkInTime;
    private Double distanceFromOffice;
    private String status;
    
    public CheckInResponse() {}
    
    public CheckInResponse(String message, Long attendanceId, Long checkInTime, Double distanceFromOffice, String status) {
        this.message = message;
        this.attendanceId = attendanceId;
        this.checkInTime = checkInTime;
        this.distanceFromOffice = distanceFromOffice;
        this.status = status;
    }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public Long getAttendanceId() { return attendanceId; }
    public void setAttendanceId(Long attendanceId) { this.attendanceId = attendanceId; }
    
    public Long getCheckInTime() { return checkInTime; }
    public void setCheckInTime(Long checkInTime) { this.checkInTime = checkInTime; }
    
    public Double getDistanceFromOffice() { return distanceFromOffice; }
    public void setDistanceFromOffice(Double distanceFromOffice) { this.distanceFromOffice = distanceFromOffice; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
} 