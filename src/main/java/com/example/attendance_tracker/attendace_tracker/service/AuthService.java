package com.example.attendance_tracker.attendace_tracker.service;

import com.example.attendance_tracker.attendace_tracker.dto.auth.AuthData;
import com.example.attendance_tracker.attendace_tracker.dto.auth.LoginRequest;
import com.example.attendance_tracker.attendace_tracker.dto.auth.UserInfo;
import com.example.attendance_tracker.attendace_tracker.dto.common.ApiResponse;
import com.example.attendance_tracker.attendace_tracker.entity.Employee;
import com.example.attendance_tracker.attendace_tracker.repository.EmployeeRepository;
import com.example.attendance_tracker.attendace_tracker.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ApiResponse<AuthData> login(LoginRequest loginRequest) {
        try {
            Optional<Employee> employeeOpt = employeeRepository.findByEmployeeId(loginRequest.getEmployeeId());
            
            if (employeeOpt.isEmpty()) {
                return ApiResponse.error("INVALID_CREDENTIALS", "Invalid employee ID or password");
            }

            Employee employee = employeeOpt.get();
            
            if (!employee.getActive()) {
                return ApiResponse.error("ACCOUNT_DISABLED", "Account is disabled");
            }

            if (!passwordEncoder.matches(loginRequest.getPassword(), employee.getPasswordHash())) {
                return ApiResponse.error("INVALID_CREDENTIALS", "Invalid employee ID or password");
            }

            String token = jwtUtil.generateToken(employee.getEmployeeId(), employee.getRole().name());
            UserInfo userInfo = new UserInfo(employee);
            AuthData authData = new AuthData(token, jwtUtil.getExpirationTime(), userInfo);

            return ApiResponse.success(authData);

        } catch (Exception e) {
            return ApiResponse.error("INTERNAL_ERROR", "An error occurred during authentication");
        }
    }
} 