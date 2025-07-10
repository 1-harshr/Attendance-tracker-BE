package com.example.attendance_tracker.attendace_tracker.service;

import com.example.attendance_tracker.attendace_tracker.entity.Employee;
import com.example.attendance_tracker.attendace_tracker.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DataInitializationService implements CommandLineRunner {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (employeeRepository.count() == 0) {
            Employee testEmployee = new Employee();
            testEmployee.setEmployeeId("EMP001");
            testEmployee.setName("Test Employee");
            testEmployee.setEmail("test@company.com");
            testEmployee.setPasswordHash(passwordEncoder.encode("password123"));
            testEmployee.setRole(Employee.Role.EMPLOYEE);
            testEmployee.setActive(true);
            employeeRepository.save(testEmployee);

            Employee testAdmin = new Employee();
            testAdmin.setEmployeeId("ADMIN001");
            testAdmin.setName("Test Admin");
            testAdmin.setEmail("admin@company.com");
            testAdmin.setPasswordHash(passwordEncoder.encode("admin123"));
            testAdmin.setRole(Employee.Role.ADMIN);
            testAdmin.setActive(true);
            employeeRepository.save(testAdmin);

            System.out.println("Test data initialized:");
            System.out.println("Employee: EMP001 / password123");
            System.out.println("Admin: ADMIN001 / admin123");
        }
    }
} 