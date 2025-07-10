package com.example.attendance_tracker.attendace_tracker.controller;

import com.example.attendance_tracker.attendace_tracker.dto.employee.*;
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
    public ResponseEntity<ApiResponse<EmployeeListResponse>> getAllEmployees() {
        ApiResponse<EmployeeListResponse> response = employeeService.getAllEmployees();
        return ResponseEntity.ok(response);
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
    public ResponseEntity<ApiResponse<EmployeeResponse>> getEmployee(@PathVariable Long id) {
        ApiResponse<EmployeeResponse> response = employeeService.getEmployeeById(id);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).body(response);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UpdateEmployeeResponse>> updateEmployee(@PathVariable Long id, @Valid @RequestBody UpdateEmployeeRequest request) {
        ApiResponse<UpdateEmployeeResponse> response = employeeService.updateEmployee(id, request);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(400).body(response);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<DeleteEmployeeResponse>> deleteEmployee(@PathVariable Long id) {
        ApiResponse<DeleteEmployeeResponse> response = employeeService.deleteEmployee(id);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(400).body(response);
        }
    }
} 