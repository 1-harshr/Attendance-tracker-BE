package com.example.attendance_tracker.attendace_tracker.controller;

import com.example.attendance_tracker.attendace_tracker.dto.attendance.*;
import com.example.attendance_tracker.attendace_tracker.dto.common.ApiResponse;
import com.example.attendance_tracker.attendace_tracker.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {
    
    @Autowired
    private AttendanceService attendanceService;
    
    @PostMapping("/check-in")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CheckInResponse>> checkIn(@Valid @RequestBody CheckInRequest request, Authentication authentication) {
        String employeeId = authentication.getName();
        ApiResponse<CheckInResponse> response = attendanceService.checkIn(employeeId, request);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(400).body(response);
        }
    }
    
    @PostMapping("/check-out")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CheckOutResponse>> checkOut(@Valid @RequestBody CheckOutRequest request, Authentication authentication) {
        String employeeId = authentication.getName();
        ApiResponse<CheckOutResponse> response = attendanceService.checkOut(employeeId, request);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(400).body(response);
        }
    }
    
    @GetMapping("/my-records")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AttendanceListResponse>> getMyAttendance(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            Authentication authentication) {
        String employeeId = authentication.getName();
        ApiResponse<AttendanceListResponse> response = attendanceService.getMyAttendanceByDateString(employeeId, startDate, endDate);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(400).body(response);
        }
    }
    
    @GetMapping("/today")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AttendanceRecordResponse>> getTodayRecord(Authentication authentication) {
        String employeeId = authentication.getName();
        ApiResponse<AttendanceRecordResponse> response = attendanceService.getTodayRecord(employeeId);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(400).body(response);
        }
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AttendanceListResponse>> getAllAttendance(
            @RequestParam(required = false) String employeeId,
            @RequestParam(required = false) Long startDate,
            @RequestParam(required = false) Long endDate) {
        ApiResponse<AttendanceListResponse> response = attendanceService.getAllAttendance(employeeId, startDate, endDate);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(400).body(response);
        }
    }
    
    @PostMapping("/manual")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ManualAttendanceResponse>> createManualAttendance(@Valid @RequestBody ManualAttendanceRequest request) {
        ApiResponse<ManualAttendanceResponse> response = attendanceService.createManualAttendance(request);
        
        if (response.isSuccess()) {
            return ResponseEntity.status(201).body(response);
        } else {
            return ResponseEntity.status(400).body(response);
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AttendanceResponse>> updateAttendance(@PathVariable Long id, @Valid @RequestBody UpdateAttendanceRequest request) {
        ApiResponse<AttendanceResponse> response = attendanceService.updateAttendance(id, request);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).body(response);
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AttendanceResponse>> deleteAttendance(@PathVariable Long id) {
        ApiResponse<AttendanceResponse> response = attendanceService.deleteAttendance(id);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).body(response);
        }
    }
    
    @GetMapping("/gps-config")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<GpsConfigResponse>> getGpsConfig() {
        ApiResponse<GpsConfigResponse> response = attendanceService.getGpsConfig();
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(400).body(response);
        }
    }
    
    @PutMapping("/gps-config")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AttendanceResponse>> updateGpsConfig(@Valid @RequestBody GpsConfigRequest request) {
        ApiResponse<AttendanceResponse> response = attendanceService.updateGpsConfig(request);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(400).body(response);
        }
    }
} 