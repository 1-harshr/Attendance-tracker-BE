package com.example.attendance_tracker.attendace_tracker.repository;

import com.example.attendance_tracker.attendace_tracker.entity.AttendanceLog;
import com.example.attendance_tracker.attendace_tracker.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Long> {
    List<AttendanceLog> findByEmployeeAndCheckInTimeBetween(Employee employee, LocalDateTime startDate, LocalDateTime endDate);
    List<AttendanceLog> findByCheckInTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT al FROM AttendanceLog al WHERE al.employee = :employee AND DATE(al.checkInTime) = DATE(:date)")
    Optional<AttendanceLog> findByEmployeeAndDate(@Param("employee") Employee employee, @Param("date") LocalDateTime date);
} 