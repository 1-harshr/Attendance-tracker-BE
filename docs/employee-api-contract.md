# Employee API Contract

## Overview
Complete CRUD (Create, Read, Update, Delete) operations for employee management. All endpoints require admin authentication with JWT Bearer tokens.

**Base URL:** `/employees`
**Authentication:** Required - Admin role (`ROLE_ADMIN`) only for all operations
**Authorization Header:** `Authorization: Bearer <jwt_token>`
**Response Format:** All responses follow the `ApiResponse<T>` pattern

---

## 1. CREATE Employee

### POST /employees

**Description:** Creates a new employee account with auto-generated employee ID.

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@company.com",
  "phone": "1234567890",
  "address": "123 Main St, City, State",
  "password": "initialPass123",
  "role": "EMPLOYEE"
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "data": {
    "message": "Employee created successfully",
    "employeeId": "EMP003"
  },
  "error": null
}
```

---

## 2. READ All Employees

### GET /employees

**Description:** Retrieves all active employees ordered by creation date (newest first).

**Request:** No body required

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "employees": [
      {
        "id": 3,
        "employeeId": "EMP002",
        "firstName": "Alice",
        "lastName": "Johnson",
        "email": "alice.johnson@company.com",
        "phone": "9876543210",
        "address": "123 Tech Park, Bangalore",
        "role": "EMPLOYEE",
        "active": true,
        "createdAt": "2025-07-10T22:28:23.141913",
        "updatedAt": "2025-07-10T22:28:23.141923"
      }
    ],
    "totalCount": 1
  },
  "error": null
}
```

---

## 3. READ Single Employee

### GET /employees/{id}

**Description:** Retrieves a specific employee by their database ID.

**Path Parameters:**
- `id` (number): Database ID of the employee

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "employeeId": "EMP001",
    "firstName": "Test",
    "lastName": "Employee",
    "email": "test@company.com",
    "phone": "1234567890",
    "address": null,
    "role": "EMPLOYEE",
    "active": true,
    "createdAt": "2025-07-10T22:28:01.897131",
    "updatedAt": "2025-07-10T22:28:01.897146"
  },
  "error": null
}
```

---

## 4. UPDATE Employee

### PUT /employees/{id}

**Description:** Updates an existing employee. All fields are optional - only provided fields will be updated.

**Path Parameters:**
- `id` (number): Database ID of the employee

**Request Body (all fields optional):**
```json
{
  "firstName": "Updated Name",
  "lastName": "New LastName",
  "email": "new.email@company.com",
  "phone": "9876543210",
  "address": "New Address",
  "password": "newPassword123",
  "role": "ADMIN",
  "active": true
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "message": "Employee updated successfully",
    "employeeId": "EMP002"
  },
  "error": null
}
```

---

## 5. DELETE Employee (Soft Delete)

### DELETE /employees/{id}

**Description:** Deactivates an employee (soft delete - sets active=false). Employee data is preserved.

**Path Parameters:**
- `id` (number): Database ID of the employee

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "message": "Employee deactivated successfully",
    "employeeId": "EMP003"
  },
  "error": null
}
```

---

## Field Requirements

### Required Fields (CREATE only):
- **firstName** (string): Employee's first name
- **phone** (string): Exactly 10 digits, no special characters
- **password** (string): Initial password set by admin

### Optional Fields:
- **lastName** (string): Employee's last name
- **email** (string): Valid email address format
- **address** (string): Physical address
- **role** (string): Either "EMPLOYEE" or "ADMIN" (defaults to "EMPLOYEE")
- **active** (boolean): Employee status (UPDATE only)

### Auto-Generated Fields:
- **employeeId** (string): Auto-generated in format "EMP001", "EMP002", etc.
- **id** (number): Database primary key
- **createdAt**, **updatedAt** (timestamp): Auto-managed timestamps

---

## Validation Rules

### Phone Number:
- Must be exactly 10 digits
- No special characters allowed (+, -, spaces, etc.)
- Regex pattern: `^\d{10}$`
- Must be unique across all employees
- Examples: ✅ "1234567890" | ❌ "+1-234-567-890" | ❌ "123456789"

### Email:
- Standard email format validation (if provided)
- Must be unique across all employees
- Examples: ✅ "user@company.com" | ❌ "invalid-email"

### Employee ID:
- Auto-generated sequential format: "EMP001", "EMP002", "EMP003"...
- No manual input required

### Role:
- Must be either "EMPLOYEE" or "ADMIN"
- Case-insensitive (converted to uppercase)

---

## HTTP Status Codes

| Operation | Success Status | Error Status | Description |
|-----------|---------------|--------------|-------------|
| POST /employees | 201 Created | 400 Bad Request | Create employee |
| GET /employees | 200 OK | 403 Forbidden | List employees |
| GET /employees/{id} | 200 OK | 404 Not Found | Get single employee |
| PUT /employees/{id} | 200 OK | 400 Bad Request | Update employee |
| DELETE /employees/{id} | 200 OK | 400 Bad Request | Soft delete employee |

---

## Error Responses

### Employee Not Found (404 Not Found):
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "EMPLOYEE_NOT_FOUND",
    "message": "Employee not found"
  }
}
```

### Employee Inactive (404 Not Found):
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "EMPLOYEE_INACTIVE",
    "message": "Employee is inactive"
  }
}
```

### Duplicate Phone (400 Bad Request):
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "DUPLICATE_PHONE",
    "message": "Phone number already exists"
  }
}
```

### Duplicate Email (400 Bad Request):
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "DUPLICATE_EMAIL",
    "message": "Email address already exists"
  }
}
```

### Invalid Role (400 Bad Request):
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "INVALID_ROLE",
    "message": "Role must be EMPLOYEE or ADMIN"
  }
}
```

### Already Inactive (400 Bad Request):
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "EMPLOYEE_ALREADY_INACTIVE",
    "message": "Employee is already inactive"
  }
}
```

### Unauthorized (403 Forbidden):
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "UNAUTHORIZED",
    "message": "Admin access required"
  }
}
```

### Validation Error (400 Bad Request):
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "VALIDATION_ERROR", 
    "message": "Phone number must be exactly 10 digits"
  }
}
```

### Invalid Phone Format (400 Bad Request):
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Phone number must be exactly 10 digits"
  }
}
```

---

## Business Rules

1. **Access Control**: Only users with ADMIN role can perform all employee operations
2. **Employee ID Generation**: Sequential auto-generation starting from EMP001
3. **Default Role**: If role is not specified during creation, defaults to "EMPLOYEE"
4. **Password Security**: Passwords are hashed using BCrypt before storage
5. **Account Status**: New employees are created with active status (active = true)
6. **Duplicate Prevention**: Phone numbers and emails (if provided) must be unique
7. **Phone Format**: Strict 10-digit validation for Indian phone numbers
8. **Soft Delete**: DELETE operation sets active=false, preserving data
9. **Active Filter**: GET operations only return active employees
10. **Partial Updates**: PUT operation supports partial updates (only provided fields are updated)

---

## Example Use Cases

### Getting Admin Token (Login First):
```bash
# Step 1: Login to get admin token
ADMIN_TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"employeeId":"ADMIN001","password":"admin123"}' | \
  jq -r '.data.token')

echo "Admin Token: $ADMIN_TOKEN"
```

### Creating a New Employee:
```bash
# Minimal employee creation
curl -X POST http://localhost:8080/employees \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "phone": "9876543210",
    "password": "temp123"
  }'

# Complete employee creation
curl -X POST http://localhost:8080/employees \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jane",
    "lastName": "Smith",
    "email": "jane.smith@company.com",
    "phone": "8765432109",
    "address": "123 Tech Park, Bangalore",
    "password": "temp123",
    "role": "EMPLOYEE"
  }'
```

### Listing All Active Employees:
```bash
curl -X GET http://localhost:8080/employees \
  -H "Authorization: Bearer $ADMIN_TOKEN"
```

### Getting Single Employee:
```bash
curl -X GET http://localhost:8080/employees/1 \
  -H "Authorization: Bearer $ADMIN_TOKEN"
```

### Updating Employee Information:
```bash
# Partial update - only specified fields
curl -X PUT http://localhost:8080/employees/3 \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jane Updated",
    "role": "ADMIN"
  }'

# Update multiple fields
curl -X PUT http://localhost:8080/employees/3 \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Alice Updated",
    "lastName": "Johnson-Smith", 
    "email": "alice.updated@company.com",
    "address": "456 New Tech Park, Bangalore"
  }'
```

### Deactivating an Employee:
```bash
curl -X DELETE http://localhost:8080/employees/3 \
  -H "Authorization: Bearer $ADMIN_TOKEN"
```

### Testing Error Scenarios:
```bash
# Test duplicate phone number
curl -X POST http://localhost:8080/employees \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Test",
    "phone": "9876543210",
    "password": "test123"
  }'

# Test invalid phone format  
curl -X PUT http://localhost:8080/employees/1 \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "phone": "12345"
  }'

# Test non-admin access (should return 403)
EMP_TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"employeeId":"EMP001","password":"password123"}' | \
  jq -r '.data.token')

curl -X GET http://localhost:8080/employees \
  -H "Authorization: Bearer $EMP_TOKEN"
```

---

## Implementation Status

### ✅ Fully Implemented & Tested
- **CREATE**: Employee creation with auto-ID generation (EMP001, EMP002...)
- **READ**: List all active employees with pagination info
- **READ**: Get single employee by database ID
- **UPDATE**: Partial employee updates with validation
- **DELETE**: Soft delete (sets active=false)
- **Validation**: Phone format, email format, duplicate prevention
- **Security**: JWT authentication, role-based access control
- **Error Handling**: Comprehensive error responses with specific codes

### 🧪 Test Results Summary
- **Employee Creation**: ✅ Auto-generated IDs EMP002, EMP003
- **Duplicate Validation**: ✅ Phone and email uniqueness enforced
- **Role-Based Security**: ✅ Admin-only access (403 for employees)
- **Soft Delete**: ✅ Preserves data, filters from active lists
- **Partial Updates**: ✅ Only provided fields updated
- **Error Handling**: ✅ Proper HTTP status codes and error messages

---

## Default Test Data

The system automatically creates test accounts on startup:

| Employee ID | Name | Role | Phone | Email | Password |
|-------------|------|------|-------|-------|----------|
| EMP001 | Test Employee | EMPLOYEE | 1234567890 | test@company.com | password123 |
| ADMIN001 | Test Admin | ADMIN | 0987654321 | admin@company.com | admin123 |

---

## Notes

- All endpoints require valid JWT token with ADMIN role
- Employee passwords are never returned in API responses
- Timestamps are in ISO 8601 format (e.g., "2025-07-10T22:28:23.141913")
- All operations maintain audit trail with createdAt/updatedAt timestamps
- Soft delete preserves data integrity for attendance records
- Phone numbers are stored and validated as exactly 10 digits for Indian numbers
- Email field is optional but must be unique if provided
- Role field defaults to "EMPLOYEE" if not specified during creation
- Update operations support partial updates (null/undefined fields are ignored) 