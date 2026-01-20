# API Documentation

Complete API reference for the Smart Assignment Submission System

## Base URL
```
http://localhost:8080/api
```

## Authentication
All protected endpoints require JWT token in Authorization header:
```
Authorization: Bearer <jwt_token>
```

---

## 🔐 Authentication Endpoints

### Register User
**POST** `/auth/register`

Create a new user account.

**Request Body:**
```json
{
  "email": "student@example.com",
  "password": "securePassword123",
  "fullName": "John Doe",
  "role": "STUDENT"  // or TEACHER
}
```

**Response (201):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "id": 1,
  "email": "student@example.com",
  "fullName": "John Doe",
  "role": "STUDENT"
}
```

**Error (409):**
```json
{
  "message": "Email already registered"
}
```

---

### Login User
**POST** `/auth/login`

Authenticate user and receive JWT token.

**Request Body:**
```json
{
  "email": "student@example.com",
  "password": "securePassword123"
}
```

**Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "id": 1,
  "email": "student@example.com",
  "fullName": "John Doe",
  "role": "STUDENT"
}
```

**Error (401):**
```json
{
  "message": "Invalid credentials"
}
```

---

### Get Current User
**GET** `/auth/me`

Get logged-in user details.

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200):**
```json
{
  "id": 1,
  "email": "student@example.com",
  "fullName": "John Doe",
  "role": "STUDENT",
  "enabled": true
}
```

---

## 📚 Assignment Endpoints

### Get All Assignments
**GET** `/assignments`

Get list of all available assignments.

**Response (200):**
```json
[
  {
    "id": 1,
    "title": "Java Programming Basics",
    "description": "Write a program to calculate factorial",
    "dueDate": "2025-12-31T23:59:59",
    "maxScore": 100,
    "teacherId": 2,
    "teacherName": "Jane Teacher",
    "createdAt": "2025-01-15T10:30:00"
  }
]
```

---

### Get Assignment by ID
**GET** `/assignments/{id}`

Get specific assignment details.

**Response (200):**
```json
{
  "id": 1,
  "title": "Java Programming Basics",
  "description": "Write a program to calculate factorial",
  "dueDate": "2025-12-31T23:59:59",
  "maxScore": 100,
  "teacherId": 2,
  "teacherName": "Jane Teacher",
  "createdAt": "2025-01-15T10:30:00"
}
```

---

### Get Teacher's Assignments
**GET** `/assignments/teacher/{teacherId}`

Get all assignments created by a teacher.

**Response (200):**
```json
[
  {
    "id": 1,
    "title": "Java Programming Basics",
    ...
  }
]
```

---

### Create Assignment
**POST** `/assignments` 
⚠️ **Requires TEACHER role**

Create a new assignment.

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "title": "Web Development Project",
  "description": "Build a responsive website using HTML, CSS, JS",
  "teacherId": 2,
  "dueDate": "2025-02-28T23:59:59",
  "maxScore": 100
}
```

**Response (200):**
```json
{
  "id": 2,
  "title": "Web Development Project",
  ...
}
```

---

### Update Assignment
**PUT** `/assignments/{id}`
⚠️ **Requires TEACHER role**

Update assignment details.

**Headers:**
```
Authorization: Bearer <token>
```

**Request Body:**
```json
{
  "title": "Updated Title",
  "description": "Updated description",
  "dueDate": "2025-03-15T23:59:59",
  "maxScore": 110
}
```

**Response (200):** Updated assignment object

---

### Delete Assignment
**DELETE** `/assignments/{id}`
⚠️ **Requires TEACHER role**

Delete an assignment.

**Response (200):**
```json
{
  "message": "Assignment deleted successfully"
}
```

---

## 📤 Submission Endpoints

### Upload Submission
**POST** `/submissions/upload`
⚠️ **Requires STUDENT role**

Submit assignment file.

**Headers:**
```
Authorization: Bearer <token>
Content-Type: multipart/form-data
```

**Parameters:**
- `assignmentId` (number): Assignment ID
- `studentId` (number): Student ID
- `file` (file): PDF, DOC, DOCX, or TXT file

**Response (200):**
```json
{
  "id": 1,
  "assignmentId": 1,
  "studentId": 3,
  "studentName": "John Student",
  "fileName": "assignment.pdf",
  "status": "SUBMITTED",
  "score": null,
  "submittedAt": "2025-01-17T14:30:00"
}
```

**Error (400):**
```json
{
  "message": "Unsupported file format"
}
```

---

### Get Submission by ID
**GET** `/submissions/{id}`

Get submission details.

**Response (200):**
```json
{
  "id": 1,
  "assignmentId": 1,
  "studentId": 3,
  "studentName": "John Student",
  "fileName": "assignment.pdf",
  "status": "PLAGIARISM_CHECK_COMPLETE",
  "score": 85,
  "feedback": "Good work!",
  "submittedAt": "2025-01-17T14:30:00",
  "plagiarismResult": {
    "id": 1,
    "similarityScore": 15.5,
    "totalComparisons": 5,
    "detailedReport": "...",
    "checkedAt": "2025-01-17T15:00:00"
  }
}
```

---

### Get Assignment Submissions
**GET** `/submissions/assignment/{assignmentId}`

Get all submissions for an assignment.

**Response (200):**
```json
[
  {
    "id": 1,
    "assignmentId": 1,
    "studentName": "John Student",
    ...
  },
  {
    "id": 2,
    "assignmentId": 1,
    "studentName": "Jane Student",
    ...
  }
]
```

---

### Get Student Submissions
**GET** `/submissions/student/{studentId}`

Get all submissions by a student.

**Response (200):**
```json
[
  {
    "id": 1,
    "assignmentId": 1,
    ...
  }
]
```

---

### Grade Submission
**POST** `/submissions/{submissionId}/grade`
⚠️ **Requires TEACHER role**

Submit grade and feedback for a submission.

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "score": 85,
  "feedback": "Excellent work! Very well structured code."
}
```

**Response (200):**
```json
{
  "id": 1,
  "status": "GRADED",
  "score": 85,
  "feedback": "Excellent work! Very well structured code.",
  ...
}
```

---

## 🔍 Plagiarism Detection Endpoints

### Check Plagiarism
**POST** `/submissions/{submissionId}/check-plagiarism`
⚠️ **Requires TEACHER role**

Run plagiarism detection on a submission.

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200):**
```json
{
  "id": 1,
  "similarityScore": 23.5,
  "totalComparisons": 8,
  "detailedReport": "=== Plagiarism Detection Report ===\nSimilarity Score: 23.50%\n...",
  "checkedAt": "2025-01-17T15:30:00"
}
```

---

### Get Plagiarism Result
**GET** `/submissions/{submissionId}/plagiarism`

Get plagiarism detection result for a submission.

**Response (200):**
```json
{
  "id": 1,
  "similarityScore": 23.5,
  "totalComparisons": 8,
  "detailedReport": "...",
  "checkedAt": "2025-01-17T15:30:00"
}
```

**Error (404):**
```json
{
  "message": "Plagiarism result not found"
}
```

---

### Get All Plagiarism Results for Assignment
**GET** `/submissions/assignment/{assignmentId}/plagiarism-results`
⚠️ **Requires TEACHER role**

Get plagiarism results for all submissions in an assignment.

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200):**
```json
[
  {
    "id": 1,
    "similarityScore": 85.5,
    "totalComparisons": 10,
    ...
  },
  {
    "id": 2,
    "similarityScore": 12.3,
    "totalComparisons": 10,
    ...
  }
]
```

Sorted by similarity score (highest first)

---

## 👥 User Management Endpoints

### Get All Users
**GET** `/users`
⚠️ **Requires ADMIN role**

Get list of all users.

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200):**
```json
[
  {
    "id": 1,
    "email": "student@example.com",
    "fullName": "John Doe",
    "role": "STUDENT",
    "enabled": true
  },
  {
    "id": 2,
    "email": "teacher@example.com",
    "fullName": "Jane Teacher",
    "role": "TEACHER",
    "enabled": true
  }
]
```

---

### Get User by ID
**GET** `/users/{id}`

Get specific user details.

**Response (200):**
```json
{
  "id": 1,
  "email": "student@example.com",
  "fullName": "John Doe",
  "role": "STUDENT",
  "enabled": true
}
```

---

### Get Users by Role
**GET** `/users/role/{role}`
⚠️ **Requires ADMIN role**

Get all users with specific role (STUDENT, TEACHER, ADMIN).

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200):**
```json
[
  {
    "id": 3,
    "email": "student1@example.com",
    "fullName": "Student One",
    "role": "STUDENT",
    "enabled": true
  }
]
```

---

### Disable User
**PUT** `/users/{id}/disable`
⚠️ **Requires ADMIN role**

Disable a user account.

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200):**
```json
{
  "message": "User disabled successfully"
}
```

---

### Enable User
**PUT** `/users/{id}/enable`
⚠️ **Requires ADMIN role**

Enable a user account.

**Response (200):**
```json
{
  "message": "User enabled successfully"
}
```

---

### Delete User
**DELETE** `/users/{id}`
⚠️ **Requires ADMIN role**

Delete a user account.

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200):**
```json
{
  "message": "User deleted successfully"
}
```

---

## Error Handling

All errors follow this format:

**400 Bad Request:**
```json
{
  "message": "Invalid input parameters"
}
```

**401 Unauthorized:**
```json
{
  "message": "Not authenticated"
}
```

**403 Forbidden:**
```json
{
  "message": "Access denied"
}
```

**404 Not Found:**
```json
{
  "message": "Resource not found"
}
```

**409 Conflict:**
```json
{
  "message": "Email already registered"
}
```

**500 Internal Server Error:**
```json
{
  "message": "Internal server error"
}
```

---

## Rate Limiting & Quotas

- **File Upload**: Max 10MB per file
- **Total Request**: Max 10MB per request
- **Token Expiration**: 24 hours

---

## Testing with cURL

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email":"test@example.com",
    "password":"password123",
    "fullName":"Test User",
    "role":"STUDENT"
  }'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'

# Get assignments (replace TOKEN)
curl -H "Authorization: Bearer TOKEN" \
  http://localhost:8080/api/assignments

# Upload submission
curl -X POST http://localhost:8080/api/submissions/upload \
  -H "Authorization: Bearer TOKEN" \
  -F "assignmentId=1" \
  -F "studentId=1" \
  -F "file=@assignment.pdf" \
  http://localhost:8080/api/submissions/upload
```

---

## Response Headers

All successful responses include:
```
Content-Type: application/json
Access-Control-Allow-Origin: *
```

---

**Last Updated**: January 2025
