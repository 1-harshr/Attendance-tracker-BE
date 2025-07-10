package com.example.attendance_tracker.attendace_tracker.dto.attendance;

public class AttendanceResponse {
    
    private String message;
    private Long attendanceId;
    
    public AttendanceResponse() {}
    
    public AttendanceResponse(String message) {
        this.message = message;
    }
    
    public AttendanceResponse(String message, Long attendanceId) {
        this.message = message;
        this.attendanceId = attendanceId;
    }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public Long getAttendanceId() { return attendanceId; }
    public void setAttendanceId(Long attendanceId) { this.attendanceId = attendanceId; }
} 