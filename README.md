# Attendance Tracker API Overview

## System Overview
The Attendance Tracker backend provides REST APIs for employee management, authentication, and attendance tracking. Built with Spring Boot, PostgreSQL, and JWT authentication.

**Base URL:** `http://localhost:8080`
**Authentication:** JWT Bearer tokens
**Database:** PostgreSQL with Hibernate ORM

---

## API Endpoints Summary

### 🔐 Authentication APIs
| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| POST | `/auth/login` | Employee login with credentials | Public |

### 👥 Employee Management APIs  
| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| POST | `/employees` | Create new employee | Admin Only |
| GET | `/employees` | List all active employees | Admin Only |
| GET | `/employees/{id}` | Get employee by ID | Admin Only |
| PUT | `/employees/{id}` | Update employee information | Admin Only |
| DELETE | `/employees/{id}` | Deactivate employee (soft delete) | Admin Only |

### 📋 Attendance APIs ✅ IMPLEMENTED
| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| POST | `/attendance/checkin` | Employee check-in with GPS | Employee/Admin |
| POST | `/attendance/checkout` | Employee check-out with GPS | Employee/Admin |
| GET | `/attendance/my-records` | Get own attendance history | Employee/Admin |
| GET | `/attendance` | Get all attendance records | Admin Only |
| POST | `/attendance/manual` | Create manual attendance | Admin Only |
| PUT | `/attendance/{id}` | Update attendance record | Admin Only |
| DELETE | `/attendance/{id}` | Delete attendance record | Admin Only |
| GET | `/attendance/gps-config` | Get GPS configuration | Admin Only |
| PUT | `/attendance/gps-config` | Update GPS configuration | Admin Only |

### 📊 Reports APIs (Placeholder)
| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| GET | `/reports/daily` | Daily attendance report | Admin Only |
| GET | `/reports/monthly` | Monthly attendance report | Admin Only |
| GET | `/reports/employee/{id}` | Employee-specific report | Admin Only |

---

## Authentication Flow

### 1. Employee Login
```bash
POST /auth/login
{
  "employeeId": "EMP001",
  "password": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzM4NCJ9...",
    "expiresIn": 86400,
    "user": {
      "id": 1,
      "employeeId": "EMP001",
      "name": "John Doe",
      "email": "john.doe@company.com",
      "role": "EMPLOYEE"
    }
  }
}
```

### 2. Using JWT Token
Include the token in subsequent requests:
```bash
Authorization: Bearer eyJhbGciOiJIUzM4NCJ9...
```

---

## Role-Based Access Control

### Admin Role (`ROLE_ADMIN`)
- **Full access** to all employee management operations
- Can create, read, update, and delete employees
- Access to all reports and attendance data
- Can manage system configuration

### Employee Role (`ROLE_EMPLOYEE`)
- **Limited access** to their own attendance operations
- Cannot access employee management endpoints
- Can view their own attendance history
- Cannot access other employees' data

---

## Data Models

### Employee Entity
```json
{
  "id": 1,
  "employeeId": "EMP001",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@company.com",
  "phone": "1234567890",
  "address": "123 Main St, City",
  "role": "EMPLOYEE",
  "active": true,
  "createdAt": "2025-07-10T22:28:01.897131",
  "updatedAt": "2025-07-10T22:28:01.897146"
}
```

### Attendance Log Entity ✅ IMPLEMENTED
```json
{
  "id": 1,
  "employeeId": "EMP001",
  "employeeName": "John Doe",
  "checkInTime": 1737021600,
  "checkOutTime": 1737054000,
  "checkInLocation": "Bangalore Office Main Building",
  "checkOutLocation": "Bangalore Office Parking",
  "distanceFromOffice": 25.5,
  "status": "CHECKED_OUT",
  "createdAt": 1737021600,
  "updatedAt": 1737054000
}
```

### Office Configuration Entity ✅ IMPLEMENTED
```json
{
  "id": 1,
  "officeLatitude": 12.9716,
  "officeLongitude": 77.5946,
  "allowedRadius": 100.0,
  "gpsValidationEnabled": false,
  "createdAt": 1737021600,
  "updatedAt": 1737021600
}
```

---

## Standard Response Format

All API responses follow the `ApiResponse<T>` pattern:

### Success Response
```json
{
  "success": true,
  "data": { /* actual response data */ },
  "error": null
}
```

### Error Response
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "ERROR_CODE",
    "message": "Human readable error message"
  }
}
```

---

## Error Codes Reference

### Authentication Errors
- `INVALID_CREDENTIALS` - Wrong employee ID or password
- `UNAUTHORIZED` - No token or invalid token
- `TOKEN_EXPIRED` - JWT token has expired

### Employee Management Errors
- `EMPLOYEE_NOT_FOUND` - Employee doesn't exist
- `EMPLOYEE_INACTIVE` - Employee is deactivated
- `DUPLICATE_PHONE` - Phone number already exists
- `DUPLICATE_EMAIL` - Email address already exists
- `INVALID_ROLE` - Invalid role specified

### Attendance Errors
- `NO_ACTIVE_CHECKIN` - No active check-in found for checkout
- `OUTSIDE_GEOFENCE` - Location is outside allowed office radius
- `ATTENDANCE_NOT_FOUND` - Attendance record not found
- `INVALID_EMPLOYEE` - Employee not found or inactive

### Validation Errors
- `VALIDATION_ERROR` - Request validation failed
- `INVALID_PHONE_FORMAT` - Phone number format invalid
- `INVALID_EMAIL_FORMAT` - Email format invalid

### System Errors
- `INTERNAL_ERROR` - Server-side error occurred

---

## HTTP Status Codes

| Status Code | Description | Usage |
|-------------|-------------|-------|
| 200 | OK | Successful GET, PUT, DELETE operations |
| 201 | Created | Successful POST operations |
| 400 | Bad Request | Validation errors, duplicate data |
| 401 | Unauthorized | Missing or invalid authentication |
| 403 | Forbidden | Insufficient permissions |
| 404 | Not Found | Resource doesn't exist |
| 500 | Internal Server Error | Server-side errors |

---

## Security Features

### JWT Token Security
- **Algorithm:** HS384 (HMAC with SHA-384)
- **Expiration:** 24 hours
- **Claims:** Employee ID, role, issued time
- **Secret:** Configured in application properties

### Password Security
- **Hashing:** BCrypt with salt
- **Storage:** Only hashed passwords stored
- **Validation:** Secure password comparison

### API Security
- **CORS:** Configured for cross-origin requests
- **CSRF:** Disabled for stateless API
- **Session:** Stateless JWT-based authentication
- **Headers:** Security headers included

---

## Development Setup

### Prerequisites
- Java 17+
- PostgreSQL 16+
- Gradle 8+

### Environment Variables
```bash
DB_HOST=localhost
DB_PORT=5432
DB_NAME=attendance_tracker
DB_USERNAME=your_username
DB_PASSWORD=your_password
JWT_SECRET=your-256-bit-secret
```

### Running the Application
```bash
./gradlew bootRun
```

### Testing with cURL
```bash
# Login
TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"employeeId":"ADMIN001","password":"admin123"}' | \
  jq -r '.data.token')

# Use the token
curl -X GET http://localhost:8080/employees \
  -H "Authorization: Bearer $TOKEN"
```

---

## Implementation Status

### ✅ Completed Features
1. **Attendance Management**
   - GPS-based check-in/check-out with Haversine distance calculation
   - Configurable geofence validation (admin controlled)
   - Real-time attendance tracking with Unix timestamps
   - Manual attendance creation and CRUD operations

2. **Office Configuration**
   - GPS coordinates configuration (Bangalore: 12.9716, 77.5946)
   - Configurable radius validation (default 100m)
   - Admin-controlled GPS validation toggle

3. **Role-Based Access Control**
   - Employee privacy (own records only)
   - Admin full access to all operations
   - JWT-based authentication with role enforcement

### 🔄 Future Enhancements
1. **Reporting System**
   - Daily/monthly attendance reports
   - Excel export functionality
   - Employee performance analytics

2. **Advanced Features**
   - Multiple office locations
   - Working hours configuration
   - Shift management
   - Leave management integration

3. **Notifications**
   - Push notifications for reminders
   - Email notifications for reports
   - SMS alerts for important events

### Scalability Considerations
- Database connection pooling (HikariCP)
- Caching layer (Redis)
- Rate limiting
- API versioning
- Monitoring and logging

---

## Documentation Links

- [Authentication API Contract](./docs/auth-api-contract.md)
- [Employee API Contract](./docs/employee-api-contract.md)
- [Attendance API Contract](./docs/attendance-api-contract.md) ✅ IMPLEMENTED
- [Backend Architecture Plan](./docs/backend-plan.md)

---

## Support

For technical support or questions:
- Review the API contracts for detailed endpoint documentation
- Check error codes and messages for troubleshooting
- Ensure proper JWT token inclusion in requests
- Verify role permissions for endpoint access 