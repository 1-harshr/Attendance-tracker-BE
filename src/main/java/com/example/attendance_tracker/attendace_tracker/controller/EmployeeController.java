package com.example.attendance_tracker.attendace_tracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    
    @GetMapping
    public ResponseEntity<?> getAllEmployees() {
        return ResponseEntity.ok("Get all employees - admin only");
    }
    
    @PostMapping
    public ResponseEntity<?> createEmployee(@RequestBody Object employeeRequest) {
        return ResponseEntity.ok("Create employee - admin only");
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