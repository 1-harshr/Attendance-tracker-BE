package com.example.attendance_tracker.attendace_tracker.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "office_config")
public class OfficeConfig {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Double officeLatitude;
    
    @Column(nullable = false)
    private Double officeLongitude;
    
    @Column(nullable = false)
    private Double allowedRadius;
    
    @Column(nullable = false)
    private Boolean gpsValidationEnabled;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
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
    
    public Double getOfficeLatitude() { return officeLatitude; }
    public void setOfficeLatitude(Double officeLatitude) { this.officeLatitude = officeLatitude; }
    
    public Double getOfficeLongitude() { return officeLongitude; }
    public void setOfficeLongitude(Double officeLongitude) { this.officeLongitude = officeLongitude; }
    
    public Double getAllowedRadius() { return allowedRadius; }
    public void setAllowedRadius(Double allowedRadius) { this.allowedRadius = allowedRadius; }
    
    public Boolean getGpsValidationEnabled() { return gpsValidationEnabled; }
    public void setGpsValidationEnabled(Boolean gpsValidationEnabled) { this.gpsValidationEnabled = gpsValidationEnabled; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
} 