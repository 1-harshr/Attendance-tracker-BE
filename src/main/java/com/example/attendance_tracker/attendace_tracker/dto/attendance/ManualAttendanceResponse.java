package com.example.attendance_tracker.attendace_tracker.dto.attendance;

public class ManualAttendanceResponse {
    
    private String message;
    private Long attendanceId;
    private String employeeId;
    
    public ManualAttendanceResponse() {}
    
    public ManualAttendanceResponse(String message, Long attendanceId, String employeeId) {
        this.message = message;
        this.attendanceId = attendanceId;
        this.employeeId = employeeId;
    }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public Long getAttendanceId() { return attendanceId; }
    public void setAttendanceId(Long attendanceId) { this.attendanceId = attendanceId; }
    
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
} 