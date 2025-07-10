package com.example.attendance_tracker.attendace_tracker.dto.employee;

import java.util.List;

public class EmployeeListResponse {
    
    private List<EmployeeResponse> employees;
    private int totalCount;
    
    public EmployeeListResponse() {}
    
    public EmployeeListResponse(List<EmployeeResponse> employees, int totalCount) {
        this.employees = employees;
        this.totalCount = totalCount;
    }
    
    public List<EmployeeResponse> getEmployees() { return employees; }
    public void setEmployees(List<EmployeeResponse> employees) { this.employees = employees; }
    
    public int getTotalCount() { return totalCount; }
    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }
} 