package com.example.attendance_tracker.attendace_tracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
public class ReportController {
    
    @GetMapping("/attendance")
    public ResponseEntity<?> exportAttendanceReport(@RequestParam(required = false) String startDate,
                                                   @RequestParam(required = false) String endDate,
                                                   @RequestParam(required = false) String employeeId) {
        return ResponseEntity.ok("Export attendance report in Excel format");
    }
} 