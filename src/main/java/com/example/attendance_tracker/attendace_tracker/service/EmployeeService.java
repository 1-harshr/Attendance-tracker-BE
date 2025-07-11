package com.example.attendance_tracker.attendace_tracker.service;

import com.example.attendance_tracker.attendace_tracker.dto.employee.*;
import com.example.attendance_tracker.attendace_tracker.dto.common.ApiResponse;
import com.example.attendance_tracker.attendace_tracker.entity.Employee;
import com.example.attendance_tracker.attendace_tracker.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            employee.setLastName(StringUtils.hasText(request.getLastName()) ? request.getLastName() : null);
            employee.setEmail(StringUtils.hasText(request.getEmail()) ? request.getEmail() : null);
            employee.setPhone(request.getPhone());
            employee.setAddress(StringUtils.hasText(request.getAddress()) ? request.getAddress() : null);
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

    public ApiResponse<EmployeeListResponse> getAllEmployees() {
        try {
            List<Employee> employees = employeeRepository.findByActiveTrueOrderByCreatedAtDesc();
            List<EmployeeResponse> employeeResponses = employees.stream()
                    .map(EmployeeResponse::new)
                    .collect(Collectors.toList());
            
            EmployeeListResponse response = new EmployeeListResponse(employeeResponses, employees.size());
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error("INTERNAL_ERROR", "An error occurred while fetching employees");
        }
    }
    
    public ApiResponse<EmployeeResponse> getEmployeeById(Long id) {
        try {
            Optional<Employee> employee = employeeRepository.findById(id);
            
            if (employee.isEmpty()) {
                return ApiResponse.error("EMPLOYEE_NOT_FOUND", "Employee not found");
            }
            
            if (!employee.get().getActive()) {
                return ApiResponse.error("EMPLOYEE_INACTIVE", "Employee is inactive");
            }
            
            EmployeeResponse response = new EmployeeResponse(employee.get());
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error("INTERNAL_ERROR", "An error occurred while fetching employee");
        }
    }
    
    public ApiResponse<UpdateEmployeeResponse> updateEmployee(Long id, UpdateEmployeeRequest request) {
        try {
            Optional<Employee> optionalEmployee = employeeRepository.findById(id);
            
            if (optionalEmployee.isEmpty()) {
                return ApiResponse.error("EMPLOYEE_NOT_FOUND", "Employee not found");
            }
            
            Employee employee = optionalEmployee.get();
            
            if (StringUtils.hasText(request.getPhone()) && 
                employeeRepository.existsByPhoneAndIdNot(request.getPhone(), id)) {
                return ApiResponse.error("DUPLICATE_PHONE", "Phone number already exists");
            }
            
            if (StringUtils.hasText(request.getEmail()) && 
                employeeRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
                return ApiResponse.error("DUPLICATE_EMAIL", "Email address already exists");
            }
            
            if (StringUtils.hasText(request.getFirstName())) {
                employee.setFirstName(request.getFirstName());
            }
            if (request.getLastName() != null) {
                employee.setLastName(StringUtils.hasText(request.getLastName()) ? request.getLastName() : null);
            }
            if (request.getEmail() != null) {
                employee.setEmail(StringUtils.hasText(request.getEmail()) ? request.getEmail() : null);
            }
            if (StringUtils.hasText(request.getPhone())) {
                employee.setPhone(request.getPhone());
            }
            if (request.getAddress() != null) {
                employee.setAddress(StringUtils.hasText(request.getAddress()) ? request.getAddress() : null);
            }
            if (StringUtils.hasText(request.getPassword())) {
                employee.setPasswordHash(passwordEncoder.encode(request.getPassword()));
            }
            if (StringUtils.hasText(request.getRole())) {
                try {
                    employee.setRole(Employee.Role.valueOf(request.getRole().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    return ApiResponse.error("INVALID_ROLE", "Role must be EMPLOYEE or ADMIN");
                }
            }
            if (request.getActive() != null) {
                employee.setActive(request.getActive());
            }
            
            Employee savedEmployee = employeeRepository.save(employee);
            
            UpdateEmployeeResponse response = new UpdateEmployeeResponse(
                "Employee updated successfully", 
                savedEmployee.getEmployeeId()
            );
            
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error("INTERNAL_ERROR", "An error occurred while updating employee");
        }
    }
    
    public ApiResponse<DeleteEmployeeResponse> deleteEmployee(Long id) {
        try {
            Optional<Employee> optionalEmployee = employeeRepository.findById(id);
            
            if (optionalEmployee.isEmpty()) {
                return ApiResponse.error("EMPLOYEE_NOT_FOUND", "Employee not found");
            }
            
            Employee employee = optionalEmployee.get();
            
            if (!employee.getActive()) {
                return ApiResponse.error("EMPLOYEE_ALREADY_INACTIVE", "Employee is already inactive");
            }
            
            employee.setActive(false);
            Employee savedEmployee = employeeRepository.save(employee);
            
            DeleteEmployeeResponse response = new DeleteEmployeeResponse(
                "Employee deactivated successfully", 
                savedEmployee.getEmployeeId()
            );
            
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error("INTERNAL_ERROR", "An error occurred while deactivating employee");
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