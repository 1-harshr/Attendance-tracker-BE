package com.example.attendance_tracker.attendace_tracker.dto.auth;

public class AuthData {
    
    private String token;
    private long expiresIn;
    private UserInfo user;
    
    public AuthData() {}
    
    public AuthData(String token, long expiresIn, UserInfo user) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.user = user;
    }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public long getExpiresIn() { return expiresIn; }
    public void setExpiresIn(long expiresIn) { this.expiresIn = expiresIn; }
    
    public UserInfo getUser() { return user; }
    public void setUser(UserInfo user) { this.user = user; }
} 