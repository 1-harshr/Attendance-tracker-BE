package com.example.attendance_tracker.attendace_tracker.service;

import com.example.attendance_tracker.attendace_tracker.dto.attendance.*;
import com.example.attendance_tracker.attendace_tracker.dto.common.ApiResponse;
import com.example.attendance_tracker.attendace_tracker.entity.AttendanceLog;
import com.example.attendance_tracker.attendace_tracker.entity.Employee;
import com.example.attendance_tracker.attendace_tracker.entity.OfficeConfig;
import com.example.attendance_tracker.attendace_tracker.repository.AttendanceLogRepository;
import com.example.attendance_tracker.attendace_tracker.repository.EmployeeRepository;
import com.example.attendance_tracker.attendace_tracker.repository.OfficeConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AttendanceService {
    
    @Autowired
    private AttendanceLogRepository attendanceLogRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private OfficeConfigRepository officeConfigRepository;
    
    private static final double EARTH_RADIUS_KM = 6371.0;
    private static final boolean GPS_VALIDATION_DEFAULT = false;
    
    public ApiResponse<CheckInResponse> checkIn(String employeeId, CheckInRequest request) {
        try {
            Optional<Employee> employeeOpt = employeeRepository.findByEmployeeId(employeeId);
            if (employeeOpt.isEmpty() || !employeeOpt.get().getActive()) {
                return ApiResponse.error("EMPLOYEE_NOT_FOUND", "Employee not found or inactive");
            }
            Employee employee = employeeOpt.get();
            
            double distance = calculateDistanceFromOffice(request.getLatitude(), request.getLongitude());
            
            if (isGpsValidationEnabled() && !isWithinAllowedRadius(distance)) {
                return ApiResponse.error("OUTSIDE_GEOFENCE", "Location is outside allowed office radius");
            }
            
            AttendanceLog attendanceLog = new AttendanceLog();
            attendanceLog.setEmployee(employee);
            attendanceLog.setCheckInTime(LocalDateTime.now());
            attendanceLog.setCheckInLocation(request.getLocation());
            attendanceLog.setDistanceFromOffice(distance);
            attendanceLog.setStatus(AttendanceLog.Status.CHECKED_IN);
            
            attendanceLog = attendanceLogRepository.save(attendanceLog);
            
            CheckInResponse response = new CheckInResponse(
                "Check-in successful",
                attendanceLog.getId(),
                attendanceLog.getCheckInTime().toEpochSecond(ZoneOffset.UTC),
                distance,
                attendanceLog.getStatus().name()
            );
            
            return ApiResponse.success(response);
            
        } catch (Exception e) {
            return ApiResponse.error("CHECK_IN_ERROR", "Error during check-in: " + e.getMessage());
        }
    }
    
    public ApiResponse<CheckOutResponse> checkOut(String employeeId, CheckOutRequest request) {
        try {
            Optional<Employee> employeeOpt = employeeRepository.findByEmployeeId(employeeId);
            if (employeeOpt.isEmpty() || !employeeOpt.get().getActive()) {
                return ApiResponse.error("EMPLOYEE_NOT_FOUND", "Employee not found or inactive");
            }
            Employee employee = employeeOpt.get();
            
            Optional<AttendanceLog> activeCheckInOpt = attendanceLogRepository
                .findTopByEmployeeAndStatusOrderByCheckInTimeDesc(employee, AttendanceLog.Status.CHECKED_IN);
            
            if (activeCheckInOpt.isEmpty()) {
                return ApiResponse.error("NO_ACTIVE_CHECKIN", "No active check-in found for checkout");
            }
            
            AttendanceLog attendanceLog = activeCheckInOpt.get();
            double distance = calculateDistanceFromOffice(request.getLatitude(), request.getLongitude());
            
            if (isGpsValidationEnabled() && !isWithinAllowedRadius(distance)) {
                return ApiResponse.error("OUTSIDE_GEOFENCE", "Location is outside allowed office radius");
            }
            
            attendanceLog.setCheckOutTime(LocalDateTime.now());
            attendanceLog.setCheckOutLocation(request.getLocation());
            attendanceLog.setStatus(AttendanceLog.Status.CHECKED_OUT);
            
            attendanceLog = attendanceLogRepository.save(attendanceLog);
            
            CheckOutResponse response = new CheckOutResponse(
                "Check-out successful",
                attendanceLog.getId(),
                attendanceLog.getCheckOutTime().toEpochSecond(ZoneOffset.UTC),
                distance,
                attendanceLog.getStatus().name()
            );
            
            return ApiResponse.success(response);
            
        } catch (Exception e) {
            return ApiResponse.error("CHECK_OUT_ERROR", "Error during check-out: " + e.getMessage());
        }
    }
    
    public ApiResponse<AttendanceListResponse> getMyAttendanceByDateString(String employeeId, String startDate, String endDate) {
        try {
            Long startTimestamp = null;
            Long endTimestamp = null;
            
            if (startDate != null && !startDate.trim().isEmpty()) {
                LocalDate startLocalDate = LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE);
                startTimestamp = startLocalDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC);
            }
            
            if (endDate != null && !endDate.trim().isEmpty()) {
                LocalDate endLocalDate = LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE);
                endTimestamp = endLocalDate.atTime(23, 59, 59).toEpochSecond(ZoneOffset.UTC);
            }
            
            return getMyAttendance(employeeId, startTimestamp, endTimestamp);
            
        } catch (Exception e) {
            return ApiResponse.error("DATE_PARSING_ERROR", "Error parsing dates: " + e.getMessage());
        }
    }
    
    public ApiResponse<AttendanceRecordResponse> getTodayRecord(String employeeId) {
        try {
            Optional<Employee> employeeOpt = employeeRepository.findByEmployeeId(employeeId);
            if (employeeOpt.isEmpty() || !employeeOpt.get().getActive()) {
                return ApiResponse.error("EMPLOYEE_NOT_FOUND", "Employee not found or inactive");
            }
            Employee employee = employeeOpt.get();
            
            LocalDate today = LocalDate.now();
            LocalDateTime startOfDay = today.atStartOfDay();
            LocalDateTime endOfDay = today.atTime(23, 59, 59);
            
            List<AttendanceLog> todayLogs = attendanceLogRepository
                .findByEmployeeAndCheckInTimeBetweenOrderByCheckInTimeDesc(employee, startOfDay, endOfDay);
            
            if (todayLogs.isEmpty()) {
                return ApiResponse.success(null);
            }
            
            AttendanceLog todayLog = todayLogs.get(0);
            AttendanceRecordResponse response = new AttendanceRecordResponse(todayLog, false);
            return ApiResponse.success(response);
            
        } catch (Exception e) {
            return ApiResponse.error("TODAY_RECORD_ERROR", "Error retrieving today's record: " + e.getMessage());
        }
    }
    
    public ApiResponse<AttendanceListResponse> getMyAttendance(String employeeId, Long startDate, Long endDate) {
        try {
            Optional<Employee> employeeOpt = employeeRepository.findByEmployeeId(employeeId);
            if (employeeOpt.isEmpty() || !employeeOpt.get().getActive()) {
                return ApiResponse.error("EMPLOYEE_NOT_FOUND", "Employee not found or inactive");
            }
            Employee employee = employeeOpt.get();
            
            LocalDateTime startDateTime = startDate != null ? 
                LocalDateTime.ofInstant(Instant.ofEpochSecond(startDate), ZoneOffset.UTC) : null;
            LocalDateTime endDateTime = endDate != null ? 
                LocalDateTime.ofInstant(Instant.ofEpochSecond(endDate), ZoneOffset.UTC) : null;
            
            List<AttendanceLog> logs;
            if (startDateTime != null && endDateTime != null) {
                logs = attendanceLogRepository.findByEmployeeAndCheckInTimeBetweenOrderByCheckInTimeDesc(
                    employee, startDateTime, endDateTime);
            } else if (startDateTime != null) {
                logs = attendanceLogRepository.findByEmployeeAndCheckInTimeGreaterThanEqualOrderByCheckInTimeDesc(
                    employee, startDateTime);
            } else if (endDateTime != null) {
                logs = attendanceLogRepository.findByEmployeeAndCheckInTimeLessThanEqualOrderByCheckInTimeDesc(
                    employee, endDateTime);
            } else {
                logs = attendanceLogRepository.findByEmployeeOrderByCheckInTimeDesc(employee);
            }
            
            List<AttendanceRecordResponse> records = logs.stream()
                .map(log -> new AttendanceRecordResponse(log, false))
                .collect(Collectors.toList());
            
            AttendanceListResponse response = new AttendanceListResponse(records, (long) records.size());
            return ApiResponse.success(response);
            
        } catch (Exception e) {
            return ApiResponse.error("ATTENDANCE_RETRIEVAL_ERROR", "Error retrieving attendance records: " + e.getMessage());
        }
    }
    
    public ApiResponse<AttendanceListResponse> getAllAttendance(String employeeId, Long startDate, Long endDate) {
        try {
            LocalDateTime startDateTime = startDate != null ? 
                LocalDateTime.ofInstant(Instant.ofEpochSecond(startDate), ZoneOffset.UTC) : null;
            LocalDateTime endDateTime = endDate != null ? 
                LocalDateTime.ofInstant(Instant.ofEpochSecond(endDate), ZoneOffset.UTC) : null;
            
            List<AttendanceLog> logs;
            if (employeeId != null) {
                Optional<Employee> employeeOpt = employeeRepository.findByEmployeeId(employeeId);
                if (employeeOpt.isEmpty() || !employeeOpt.get().getActive()) {
                    return ApiResponse.error("EMPLOYEE_NOT_FOUND", "Employee not found or inactive");
                }
                Employee employee = employeeOpt.get();
                
                if (startDateTime != null && endDateTime != null) {
                    logs = attendanceLogRepository.findByEmployeeAndCheckInTimeBetweenOrderByCheckInTimeDesc(
                        employee, startDateTime, endDateTime);
                } else if (startDateTime != null) {
                    logs = attendanceLogRepository.findByEmployeeAndCheckInTimeGreaterThanEqualOrderByCheckInTimeDesc(
                        employee, startDateTime);
                } else if (endDateTime != null) {
                    logs = attendanceLogRepository.findByEmployeeAndCheckInTimeLessThanEqualOrderByCheckInTimeDesc(
                        employee, endDateTime);
                } else {
                    logs = attendanceLogRepository.findByEmployeeOrderByCheckInTimeDesc(employee);
                }
            } else {
                if (startDateTime != null && endDateTime != null) {
                    logs = attendanceLogRepository.findByCheckInTimeBetweenOrderByCheckInTimeDesc(
                        startDateTime, endDateTime);
                } else if (startDateTime != null) {
                    logs = attendanceLogRepository.findByCheckInTimeGreaterThanEqualOrderByCheckInTimeDesc(
                        startDateTime);
                } else if (endDateTime != null) {
                    logs = attendanceLogRepository.findByCheckInTimeLessThanEqualOrderByCheckInTimeDesc(
                        endDateTime);
                } else {
                    logs = attendanceLogRepository.findAllByOrderByCheckInTimeDesc();
                }
            }
            
            List<AttendanceRecordResponse> records = logs.stream()
                .map(log -> new AttendanceRecordResponse(log, true))
                .collect(Collectors.toList());
            
            AttendanceListResponse response = new AttendanceListResponse(records, (long) records.size());
            return ApiResponse.success(response);
            
        } catch (Exception e) {
            return ApiResponse.error("ATTENDANCE_RETRIEVAL_ERROR", "Error retrieving attendance records: " + e.getMessage());
        }
    }
    
    public ApiResponse<ManualAttendanceResponse> createManualAttendance(ManualAttendanceRequest request) {
        try {
            Optional<Employee> employeeOpt = employeeRepository.findByEmployeeId(request.getEmployeeId());
            if (employeeOpt.isEmpty() || !employeeOpt.get().getActive()) {
                return ApiResponse.error("EMPLOYEE_NOT_FOUND", "Employee not found or inactive");
            }
            Employee employee = employeeOpt.get();
            
            AttendanceLog attendanceLog = new AttendanceLog();
            attendanceLog.setEmployee(employee);
            attendanceLog.setCheckInTime(LocalDateTime.ofInstant(
                Instant.ofEpochSecond(request.getCheckInTime()), ZoneOffset.UTC));
            
            if (request.getCheckOutTime() != null) {
                attendanceLog.setCheckOutTime(LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(request.getCheckOutTime()), ZoneOffset.UTC));
            }
            
            attendanceLog.setCheckInLocation("Manual Entry");
            attendanceLog.setCheckOutLocation(request.getCheckOutTime() != null ? "Manual Entry" : null);
            attendanceLog.setDistanceFromOffice(0.0);
            attendanceLog.setStatus(AttendanceLog.Status.valueOf(request.getStatus()));
            
            attendanceLog = attendanceLogRepository.save(attendanceLog);
            
            ManualAttendanceResponse response = new ManualAttendanceResponse(
                "Manual attendance created successfully",
                attendanceLog.getId(),
                employee.getEmployeeId()
            );
            
            return ApiResponse.success(response);
            
        } catch (Exception e) {
            return ApiResponse.error("MANUAL_ATTENDANCE_ERROR", "Error creating manual attendance: " + e.getMessage());
        }
    }
    
    public ApiResponse<AttendanceResponse> updateAttendance(Long attendanceId, UpdateAttendanceRequest request) {
        try {
            Optional<AttendanceLog> attendanceLogOpt = attendanceLogRepository.findById(attendanceId);
            if (attendanceLogOpt.isEmpty()) {
                return ApiResponse.error("ATTENDANCE_NOT_FOUND", "Attendance record not found");
            }
            
            AttendanceLog attendanceLog = attendanceLogOpt.get();
            
            if (request.getCheckInTime() != null) {
                attendanceLog.setCheckInTime(LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(request.getCheckInTime()), ZoneOffset.UTC));
            }
            
            if (request.getCheckOutTime() != null) {
                attendanceLog.setCheckOutTime(LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(request.getCheckOutTime()), ZoneOffset.UTC));
            }
            
            if (request.getStatus() != null) {
                attendanceLog.setStatus(AttendanceLog.Status.valueOf(request.getStatus()));
            }
            
            attendanceLogRepository.save(attendanceLog);
            
            AttendanceResponse response = new AttendanceResponse(
                "Attendance updated successfully",
                attendanceLog.getId()
            );
            
            return ApiResponse.success(response);
            
        } catch (Exception e) {
            return ApiResponse.error("ATTENDANCE_UPDATE_ERROR", "Error updating attendance: " + e.getMessage());
        }
    }
    
    public ApiResponse<AttendanceResponse> deleteAttendance(Long attendanceId) {
        try {
            Optional<AttendanceLog> attendanceLogOpt = attendanceLogRepository.findById(attendanceId);
            if (attendanceLogOpt.isEmpty()) {
                return ApiResponse.error("ATTENDANCE_NOT_FOUND", "Attendance record not found");
            }
            
            attendanceLogRepository.deleteById(attendanceId);
            
            AttendanceResponse response = new AttendanceResponse(
                "Attendance record deleted successfully",
                attendanceId
            );
            
            return ApiResponse.success(response);
            
        } catch (Exception e) {
            return ApiResponse.error("ATTENDANCE_DELETE_ERROR", "Error deleting attendance: " + e.getMessage());
        }
    }
    
    public ApiResponse<GpsConfigResponse> getGpsConfig() {
        try {
            OfficeConfig config = getOrCreateOfficeConfig();
            
            GpsConfigResponse response = new GpsConfigResponse(
                config.getGpsValidationEnabled(),
                config.getOfficeLatitude(),
                config.getOfficeLongitude(),
                config.getAllowedRadius()
            );
            
            return ApiResponse.success(response);
            
        } catch (Exception e) {
            return ApiResponse.error("GPS_CONFIG_ERROR", "Error retrieving GPS configuration: " + e.getMessage());
        }
    }
    
    public ApiResponse<AttendanceResponse> updateGpsConfig(GpsConfigRequest request) {
        try {
            OfficeConfig config = getOrCreateOfficeConfig();
            
            if (request.getGpsValidationEnabled() != null) {
                config.setGpsValidationEnabled(request.getGpsValidationEnabled());
            }
            
            if (request.getOfficeLatitude() != null) {
                config.setOfficeLatitude(request.getOfficeLatitude());
            }
            
            if (request.getOfficeLongitude() != null) {
                config.setOfficeLongitude(request.getOfficeLongitude());
            }
            
            if (request.getAllowedRadius() != null) {
                config.setAllowedRadius(request.getAllowedRadius());
            }
            
            officeConfigRepository.save(config);
            
            AttendanceResponse response = new AttendanceResponse("GPS configuration updated successfully");
            return ApiResponse.success(response);
            
        } catch (Exception e) {
            return ApiResponse.error("GPS_CONFIG_UPDATE_ERROR", "Error updating GPS configuration: " + e.getMessage());
        }
    }
    
    private double calculateDistanceFromOffice(double lat, double lon) {
        try {
            OfficeConfig config = getOrCreateOfficeConfig();
            return calculateDistance(lat, lon, config.getOfficeLatitude(), config.getOfficeLongitude());
        } catch (Exception e) {
            return 0.0;
        }
    }
    
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return EARTH_RADIUS_KM * c * 1000;
    }
    
    private boolean isGpsValidationEnabled() {
        try {
            OfficeConfig config = getOrCreateOfficeConfig();
            return config.getGpsValidationEnabled();
        } catch (Exception e) {
            return GPS_VALIDATION_DEFAULT;
        }
    }
    
    private boolean isWithinAllowedRadius(double distance) {
        try {
            OfficeConfig config = getOrCreateOfficeConfig();
            return distance <= config.getAllowedRadius();
        } catch (Exception e) {
            return true;
        }
    }
    
    private OfficeConfig getOrCreateOfficeConfig() {
        List<OfficeConfig> configs = officeConfigRepository.findAll();
        if (configs.isEmpty()) {
            OfficeConfig config = new OfficeConfig();
            config.setOfficeLatitude(12.9716);
            config.setOfficeLongitude(77.5946);
            config.setAllowedRadius(100.0);
            config.setGpsValidationEnabled(GPS_VALIDATION_DEFAULT);
            return officeConfigRepository.save(config);
        }
        return configs.get(0);
    }
} 