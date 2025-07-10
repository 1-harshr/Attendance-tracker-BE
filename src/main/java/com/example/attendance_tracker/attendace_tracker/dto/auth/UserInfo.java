package com.example.attendance_tracker.attendace_tracker.dto.auth;

import com.example.attendance_tracker.attendace_tracker.entity.Employee;

public class UserInfo {
    
    private Long id;
    private String employeeId;
    private String name;
    private String email;
    private String role;
    
    public UserInfo() {}
    
    public UserInfo(Employee employee) {
        this.id = employee.getId();
        this.employeeId = employee.getEmployeeId();
        this.name = employee.getName();
        this.email = employee.getEmail();
        this.role = employee.getRole().name();
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
} 