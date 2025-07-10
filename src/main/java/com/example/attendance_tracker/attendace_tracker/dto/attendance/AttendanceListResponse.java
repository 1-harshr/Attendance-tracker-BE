package com.example.attendance_tracker.attendace_tracker.dto.attendance;

import java.util.List;

public class AttendanceListResponse {
    
    private List<AttendanceRecordResponse> records;
    private Long totalCount;
    
    public AttendanceListResponse() {}
    
    public AttendanceListResponse(List<AttendanceRecordResponse> records, Long totalCount) {
        this.records = records;
        this.totalCount = totalCount;
    }
    
    public List<AttendanceRecordResponse> getRecords() { return records; }
    public void setRecords(List<AttendanceRecordResponse> records) { this.records = records; }
    
    public Long getTotalCount() { return totalCount; }
    public void setTotalCount(Long totalCount) { this.totalCount = totalCount; }
} 