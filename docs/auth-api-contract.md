# Auth API Contract

## Authentication Endpoint

### POST /auth/login

**Description:** Authenticates employee and returns JWT token

**Request Body:**
```json
{
  "employeeId": "EMP001",
  "password": "password123"
}
```

**Success Response (200):**
```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresIn": 86400,
    "user": {
      "id": 1,
      "employeeId": "EMP001",
      "name": "John Doe",
      "email": "john@company.com",
      "role": "EMPLOYEE"
    }
  }
}
```

**Error Response (401):**
```json
{
  "success": false,
  "error": {
    "code": "INVALID_CREDENTIALS",
    "message": "Invalid employee ID or password"
  }
}
```

**Error Response (400):**
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Employee ID and password are required"
  }
}
```

## Token Usage

**Header:** `Authorization: Bearer <token>`

**Invalid Token Response (401):**
```json
{
  "success": false,
  "error": {
    "code": "INVALID_TOKEN",
    "message": "Token is invalid or expired"
  }
}
```

## Security Features

- JWT tokens with 24-hour expiration
- BCrypt password hashing
- HTTPS required for production
- Employee ID based authentication
- Role-based access control (EMPLOYEE/ADMIN) 