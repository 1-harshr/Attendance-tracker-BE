package com.example.attendance_tracker.attendace_tracker.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance_logs")
public class AttendanceLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    
    @Column(nullable = false)
    private LocalDateTime checkInTime;
    
    private LocalDateTime checkOutTime;
    
    @Column(nullable = false)
    private String checkInLocation;
    
    private String checkOutLocation;
    
    @Column(nullable = false)
    private Double distanceFromOffice;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    public enum Status {
        CHECKED_IN, CHECKED_OUT, INCOMPLETE
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    
    public LocalDateTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalDateTime checkInTime) { this.checkInTime = checkInTime; }
    
    public LocalDateTime getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(LocalDateTime checkOutTime) { this.checkOutTime = checkOutTime; }
    
    public String getCheckInLocation() { return checkInLocation; }
    public void setCheckInLocation(String checkInLocation) { this.checkInLocation = checkInLocation; }
    
    public String getCheckOutLocation() { return checkOutLocation; }
    public void setCheckOutLocation(String checkOutLocation) { this.checkOutLocation = checkOutLocation; }
    
    public Double getDistanceFromOffice() { return distanceFromOffice; }
    public void setDistanceFromOffice(Double distanceFromOffice) { this.distanceFromOffice = distanceFromOffice; }
    
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
} 