package com.example.attendance_tracker.attendace_tracker.service;

import com.example.attendance_tracker.attendace_tracker.dto.employee.CreateEmployeeRequest;
import com.example.attendance_tracker.attendace_tracker.dto.employee.CreateEmployeeResponse;
import com.example.attendance_tracker.attendace_tracker.dto.common.ApiResponse;
import com.example.attendance_tracker.attendace_tracker.entity.Employee;
import com.example.attendance_tracker.attendace_tracker.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ApiResponse<CreateEmployeeResponse> createEmployee(CreateEmployeeRequest request) {
        try {
            if (employeeRepository.existsByPhone(request.getPhone())) {
                return ApiResponse.error("DUPLICATE_PHONE", "Phone number already exists");
            }

            if (StringUtils.hasText(request.getEmail()) && employeeRepository.existsByEmail(request.getEmail())) {
                return ApiResponse.error("DUPLICATE_EMAIL", "Email address already exists");
            }

            String employeeId = generateNextEmployeeId();

            Employee employee = new Employee();
            employee.setEmployeeId(employeeId);
            employee.setFirstName(request.getFirstName());
            employee.setLastName(request.getLastName());
            employee.setEmail(request.getEmail());
            employee.setPhone(request.getPhone());
            employee.setAddress(request.getAddress());
            employee.setPasswordHash(passwordEncoder.encode(request.getPassword()));
            
            String role = StringUtils.hasText(request.getRole()) ? request.getRole().toUpperCase() : "EMPLOYEE";
            try {
                employee.setRole(Employee.Role.valueOf(role));
            } catch (IllegalArgumentException e) {
                return ApiResponse.error("INVALID_ROLE", "Role must be EMPLOYEE or ADMIN");
            }
            
            employee.setActive(true);

            Employee savedEmployee = employeeRepository.save(employee);

            CreateEmployeeResponse response = new CreateEmployeeResponse(
                "Employee created successfully", 
                savedEmployee.getEmployeeId()
            );

            return ApiResponse.success(response);

        } catch (Exception e) {
            return ApiResponse.error("INTERNAL_ERROR", "An error occurred while creating employee");
        }
    }

    private String generateNextEmployeeId() {
        Optional<Employee> lastEmployee = employeeRepository.findTopByOrderByEmployeeIdDesc();
        
        if (lastEmployee.isEmpty()) {
            return "EMP001";
        }
        
        String lastId = lastEmployee.get().getEmployeeId();
        try {
            int lastNumber = Integer.parseInt(lastId.substring(3));
            int nextNumber = lastNumber + 1;
            return String.format("EMP%03d", nextNumber);
        } catch (Exception e) {
            return "EMP001";
        }
    }
} 