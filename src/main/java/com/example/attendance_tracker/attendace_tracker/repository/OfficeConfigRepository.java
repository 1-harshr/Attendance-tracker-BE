package com.example.attendance_tracker.attendace_tracker.repository;

import com.example.attendance_tracker.attendace_tracker.entity.OfficeConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfficeConfigRepository extends JpaRepository<OfficeConfig, Long> {
} 