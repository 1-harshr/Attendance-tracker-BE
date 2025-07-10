package com.example.attendance_tracker.attendace_tracker.dto.attendance;

import com.example.attendance_tracker.attendace_tracker.entity.AttendanceLog;
import java.time.ZoneOffset;

public class AttendanceRecordResponse {
    
    private Long id;
    private String employeeId;
    private String employeeName;
    private Long checkInTime;
    private Long checkOutTime;
    private String checkInLocation;
    private String checkOutLocation;
    private Double distanceFromOffice;
    private String status;
    private Long createdAt;
    private Long updatedAt;
    
    public AttendanceRecordResponse() {}
    
    public AttendanceRecordResponse(AttendanceLog attendanceLog, boolean includeEmployeeInfo) {
        this.id = attendanceLog.getId();
        this.checkInTime = attendanceLog.getCheckInTime() != null ? 
            attendanceLog.getCheckInTime().toEpochSecond(ZoneOffset.UTC) : null;
        this.checkOutTime = attendanceLog.getCheckOutTime() != null ? 
            attendanceLog.getCheckOutTime().toEpochSecond(ZoneOffset.UTC) : null;
        this.checkInLocation = attendanceLog.getCheckInLocation();
        this.checkOutLocation = attendanceLog.getCheckOutLocation();
        this.distanceFromOffice = attendanceLog.getDistanceFromOffice();
        this.status = attendanceLog.getStatus().name();
        this.createdAt = attendanceLog.getCreatedAt().toEpochSecond(ZoneOffset.UTC);
        this.updatedAt = attendanceLog.getUpdatedAt().toEpochSecond(ZoneOffset.UTC);
        
        if (includeEmployeeInfo && attendanceLog.getEmployee() != null) {
            this.employeeId = attendanceLog.getEmployee().getEmployeeId();
            this.employeeName = attendanceLog.getEmployee().getFirstName() + " " + 
                (attendanceLog.getEmployee().getLastName() != null ? attendanceLog.getEmployee().getLastName() : "");
        }
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    
    public Long getCheckInTime() { return checkInTime; }
    public void setCheckInTime(Long checkInTime) { this.checkInTime = checkInTime; }
    
    public Long getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(Long checkOutTime) { this.checkOutTime = checkOutTime; }
    
    public String getCheckInLocation() { return checkInLocation; }
    public void setCheckInLocation(String checkInLocation) { this.checkInLocation = checkInLocation; }
    
    public String getCheckOutLocation() { return checkOutLocation; }
    public void setCheckOutLocation(String checkOutLocation) { this.checkOutLocation = checkOutLocation; }
    
    public Double getDistanceFromOffice() { return distanceFromOffice; }
    public void setDistanceFromOffice(Double distanceFromOffice) { this.distanceFromOffice = distanceFromOffice; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Long getCreatedAt() { return createdAt; }
    public void setCreatedAt(Long createdAt) { this.createdAt = createdAt; }
    
    public Long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Long updatedAt) { this.updatedAt = updatedAt; }
} 