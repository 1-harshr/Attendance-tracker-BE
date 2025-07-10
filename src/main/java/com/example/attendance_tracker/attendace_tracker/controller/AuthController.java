package com.example.attendance_tracker.attendace_tracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Object loginRequest) {
        return ResponseEntity.ok("Login endpoint - implement later");
    }
} 