package com.example.attendance_tracker.attendace_tracker.dto.attendance;

public class GpsConfigResponse {
    
    private Boolean gpsValidationEnabled;
    private Double officeLatitude;
    private Double officeLongitude;
    private Double allowedRadius;
    
    public GpsConfigResponse() {}
    
    public GpsConfigResponse(Boolean gpsValidationEnabled, Double officeLatitude, Double officeLongitude, Double allowedRadius) {
        this.gpsValidationEnabled = gpsValidationEnabled;
        this.officeLatitude = officeLatitude;
        this.officeLongitude = officeLongitude;
        this.allowedRadius = allowedRadius;
    }
    
    public Boolean getGpsValidationEnabled() { return gpsValidationEnabled; }
    public void setGpsValidationEnabled(Boolean gpsValidationEnabled) { this.gpsValidationEnabled = gpsValidationEnabled; }
    
    public Double getOfficeLatitude() { return officeLatitude; }
    public void setOfficeLatitude(Double officeLatitude) { this.officeLatitude = officeLatitude; }
    
    public Double getOfficeLongitude() { return officeLongitude; }
    public void setOfficeLongitude(Double officeLongitude) { this.officeLongitude = officeLongitude; }
    
    public Double getAllowedRadius() { return allowedRadius; }
    public void setAllowedRadius(Double allowedRadius) { this.allowedRadius = allowedRadius; }
} 