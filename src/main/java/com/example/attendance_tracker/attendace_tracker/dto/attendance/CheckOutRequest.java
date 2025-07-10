package com.example.attendance_tracker.attendace_tracker.dto.attendance;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

public class CheckOutRequest {
    
    @NotNull(message = "Latitude is required")
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    private Double latitude;
    
    @NotNull(message = "Longitude is required")
    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    private Double longitude;
    
    @NotNull(message = "Location is required")
    private String location;
    
    public CheckOutRequest() {}
    
    public CheckOutRequest(Double latitude, Double longitude, String location) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
    }
    
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
} 