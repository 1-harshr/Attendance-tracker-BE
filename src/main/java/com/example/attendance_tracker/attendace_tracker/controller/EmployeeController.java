package com.example.attendance_tracker.attendace_tracker.controller;

import com.example.attendance_tracker.attendace_tracker.dto.employee.CreateEmployeeRequest;
import com.example.attendance_tracker.attendace_tracker.dto.employee.CreateEmployeeResponse;
import com.example.attendance_tracker.attendace_tracker.dto.common.ApiResponse;
import com.example.attendance_tracker.attendace_tracker.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    
    @Autowired
    private EmployeeService employeeService;
    
    @GetMapping
    public ResponseEntity<?> getAllEmployees() {
        return ResponseEntity.ok("Get all employees - admin only");
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<CreateEmployeeResponse>> createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        ApiResponse<CreateEmployeeResponse> response = employeeService.createEmployee(request);
        
        if (response.isSuccess()) {
            return ResponseEntity.status(201).body(response);
        } else {
            return ResponseEntity.status(400).body(response);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable Long id) {
        return ResponseEntity.ok("Get employee by ID");
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long id, @RequestBody Object employeeRequest) {
        return ResponseEntity.ok("Update employee - admin only");
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        return ResponseEntity.ok("Deactivate employee - admin only");
    }
} 