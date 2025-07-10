package com.example.attendance_tracker.attendace_tracker.repository;

import com.example.attendance_tracker.attendace_tracker.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmployeeId(String employeeId);
    Optional<Employee> findByEmail(String email);
    Optional<Employee> findByPhone(String phone);
    boolean existsByEmployeeId(String employeeId);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Optional<Employee> findTopByOrderByEmployeeIdDesc();
} 