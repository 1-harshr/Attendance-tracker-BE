# Employee API Contract

## Employee Creation Endpoint

### POST /employees (Admin Only)

**Description:** Creates a new employee account. Only admins can create employees.

**Authentication:** Required - Admin role only

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

## Field Requirements

### Required Fields:
- **firstName** (string): Employee's first name
- **phone** (string): Exactly 10 digits, no special characters
- **password** (string): Initial password set by admin

### Optional Fields:
- **lastName** (string): Employee's last name
- **email** (string): Valid email address format
- **address** (string): Physical address
- **role** (string): Either "EMPLOYEE" or "ADMIN" (defaults to "EMPLOYEE")

### Auto-Generated Fields:
- **employeeId** (string): Auto-generated in format "EMP001", "EMP002", etc.
- **id** (number): Database primary key
- **active** (boolean): Defaults to true
- **createdAt**, **updatedAt** (timestamp): Auto-managed timestamps

## Validation Rules

### Phone Number:
- Must be exactly 10 digits
- No special characters allowed (+, -, spaces, etc.)
- Regex pattern: `^\d{10}$`
- Examples: ✅ "1234567890" | ❌ "+1-234-567-890" | ❌ "123456789"

### Email:
- Standard email format validation (if provided)
- Examples: ✅ "user@company.com" | ❌ "invalid-email"

### Employee ID:
- Auto-generated sequential format: "EMP001", "EMP002", "EMP003"...
- No manual input required

## Response Formats

### Success Response (201 Created):
```json
{
  "success": true,
  "message": "Employee created successfully",
  "employeeId": "EMP003"
}
```

### Validation Error Response (400 Bad Request):
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Phone number must be exactly 10 digits"
  }
}
```

### Duplicate Phone Error Response (400 Bad Request):
```json
{
  "success": false,
  "error": {
    "code": "DUPLICATE_PHONE",
    "message": "Phone number already exists"
  }
}
```

### Duplicate Email Error Response (400 Bad Request):
```json
{
  "success": false,
  "error": {
    "code": "DUPLICATE_EMAIL",
    "message": "Email address already exists"
  }
}
```

### Unauthorized Error Response (401 Unauthorized):
```json
{
  "success": false,
  "error": {
    "code": "UNAUTHORIZED",
    "message": "Admin access required"
  }
}
```

## Business Rules

1. **Access Control**: Only users with ADMIN role can create employees
2. **Employee ID Generation**: Sequential auto-generation starting from EMP001
3. **Default Role**: If role is not specified, defaults to "EMPLOYEE"
4. **Password Security**: Passwords are hashed using BCrypt before storage
5. **Account Status**: New employees are created with active status (active = true)
6. **Duplicate Prevention**: Phone numbers and emails (if provided) must be unique
7. **Phone Format**: Strict 10-digit validation for Indian phone numbers

## Example Requests

### Minimal Employee Creation:
```json
{
  "firstName": "Jane",
  "phone": "9876543210",
  "password": "temp123"
}
```

### Complete Employee Creation:
```json
{
  "firstName": "John",
  "lastName": "Smith",
  "email": "john.smith@company.com",
  "phone": "1234567890",
  "address": "456 Business Ave, Mumbai, Maharashtra",
  "password": "secure456",
  "role": "ADMIN"
}
``` 