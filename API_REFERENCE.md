# Complete API Reference - All Endpoints

## 🔐 Authentication Endpoints (Public)

### Register New User
```
POST /api/auth/register
Content-Type: application/json
Authorization: NOT REQUIRED ✓

Request Body:
{
  "email": "user@example.com",
  "password": "password123",
  "role": "STUDENT"  // STUDENT, CREATOR, or ADMIN
}

Response: 200 OK
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "role": "STUDENT"
}

Error Responses:
- 400 Bad Request: Invalid email format or password too short
- 409 Conflict: Email already exists
```

### Login
```
POST /api/auth/login
Content-Type: application/json
Authorization: NOT REQUIRED ✓

Request Body:
{
  "email": "user@example.com",
  "password": "password123"
}

Response: 200 OK
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "role": "STUDENT"
}

Error Responses:
- 401 Unauthorized: Invalid email or password
- 400 Bad Request: Missing fields
```

### Refresh Token
```
POST /api/auth/refresh
Authorization: Bearer {refreshToken}
Content-Type: application/json

Request Body: {} (empty)

Response: 200 OK
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "role": "STUDENT"
}

Error Responses:
- 401 Unauthorized: Invalid refresh token
- 401 Unauthorized: Token expired
```

### Validate Token
```
GET /api/auth/validate
Authorization: Bearer {accessToken}

Response: 200 OK (empty body, just status)

Error Responses:
- 401 Unauthorized: Invalid or expired token
```

## 👥 Groups Endpoints

### Get All Groups (Public)
```
GET /api/groups?page=0&size=10
Authorization: NOT REQUIRED ✓
Query Parameters:
  - page: Page number (default: 0)
  - size: Items per page (default: 10)

Response: 200 OK
{
  "content": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "name": "Math Study Group",
      "description": "Advanced Calculus",
      "subject": "Mathematics",
      "location": "Online",
      "creatorId": "550e8400-e29b-41d4-a716-446655440001",
      "createdAt": "2026-05-10T10:30:00",
      "status": "APPROVED",
      "memberCount": 15
    }
  ],
  "totalElements": 50,
  "totalPages": 5,
  "currentPage": 0,
  "size": 10
}

Error Responses:
- 400 Bad Request: Invalid page/size parameters
```

### Get Single Group (Public)
```
GET /api/groups/{groupId}
Authorization: NOT REQUIRED ✓

Response: 200 OK
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Math Study Group",
  "description": "Advanced Calculus",
  "subject": "Mathematics",
  "location": "Online",
  "creatorId": "550e8400-e29b-41d4-a716-446655440001",
  "createdAt": "2026-05-10T10:30:00",
  "status": "APPROVED",
  "memberCount": 15
}

Error Responses:
- 404 Not Found: Group doesn't exist
```

### Search Groups (Public)
```
GET /api/groups/search?q=math&subject=Mathematics&location=Online
Authorization: NOT REQUIRED ✓
Query Parameters:
  - q: Search query (optional)
  - subject: Subject filter (optional)
  - location: Location filter (optional)

Response: 200 OK
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Math Study Group",
    ...
  }
]
```

### Create Group (Protected - CREATOR/ADMIN)
```
POST /api/groups
Authorization: Bearer {accessToken} ✓ REQUIRED
Content-Type: application/json
X-User-Id: Auto-added by gateway
X-User-Role: Auto-added by gateway

Request Body:
{
  "name": "Physics Study Group",
  "description": "Quantum Mechanics",
  "subject": "Physics",
  "location": "Room 101, Science Building"
}

Response: 201 Created
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Physics Study Group",
  "description": "Quantum Mechanics",
  "subject": "Physics",
  "location": "Room 101, Science Building",
  "creatorId": "your-user-id",
  "createdAt": "2026-05-10T10:30:00",
  "status": "PENDING",
  "memberCount": 1
}

Error Responses:
- 401 Unauthorized: Missing token
- 403 Forbidden: Don't have CREATOR role
- 400 Bad Request: Missing required fields
```

### Update Group (Protected - Group Creator only)
```
PUT /api/groups/{groupId}
Authorization: Bearer {accessToken} ✓ REQUIRED
Content-Type: application/json
X-User-Id: Auto-added by gateway
X-User-Role: Auto-added by gateway

Request Body:
{
  "name": "Physics Study Group (Updated)",
  "description": "Quantum Mechanics & Special Relativity",
  "subject": "Physics",
  "location": "Room 102"
}

Response: 200 OK
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Physics Study Group (Updated)",
  ...
}

Error Responses:
- 401 Unauthorized: Missing token
- 403 Forbidden: Not the group creator
- 404 Not Found: Group doesn't exist
```

### Delete Group (Protected - Group Creator only)
```
DELETE /api/groups/{groupId}
Authorization: Bearer {accessToken} ✓ REQUIRED
X-User-Id: Auto-added by gateway
X-User-Role: Auto-added by gateway

Response: 204 No Content

Error Responses:
- 401 Unauthorized: Missing token
- 403 Forbidden: Not the group creator
- 404 Not Found: Group doesn't exist
```

### Approve Group (Protected - ADMIN only)
```
PUT /api/groups/{groupId}/approve
Authorization: Bearer {accessToken} ✓ REQUIRED
X-User-Id: Auto-added by gateway
X-User-Role: Auto-added by gateway

Response: 200 OK

Error Responses:
- 401 Unauthorized: Missing token
- 403 Forbidden: Not an ADMIN
- 404 Not Found: Group doesn't exist
```

### Reject Group (Protected - ADMIN only)
```
PUT /api/groups/{groupId}/reject
Authorization: Bearer {accessToken} ✓ REQUIRED
X-User-Id: Auto-added by gateway
X-User-Role: Auto-added by gateway

Response: 200 OK

Error Responses:
- 401 Unauthorized: Missing token
- 403 Forbidden: Not an ADMIN
- 404 Not Found: Group doesn't exist
```

### Get Group Members (Public)
```
GET /api/groups/{groupId}/members
Authorization: NOT REQUIRED ✓

Response: 200 OK
[
  "550e8400-e29b-41d4-a716-446655440001",
  "550e8400-e29b-41d4-a716-446655440002",
  "550e8400-e29b-41d4-a716-446655440003"
]

Error Responses:
- 404 Not Found: Group doesn't exist
```

## 📝 Discussion Endpoints

### Get All Posts (Public)
```
GET /api/posts?page=0&size=10
Authorization: NOT REQUIRED ✓

Response: 200 OK
{
  "content": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "groupId": "550e8400-e29b-41d4-a716-446655440010",
      "userId": "550e8400-e29b-41d4-a716-446655440001",
      "title": "Quantum Entanglement",
      "content": "Can someone explain quantum entanglement?",
      "createdAt": "2026-05-10T10:30:00"
    }
  ],
  "totalElements": 100,
  "totalPages": 10,
  "currentPage": 0
}
```

### Create Post (Protected)
```
POST /api/posts
Authorization: Bearer {accessToken} ✓ REQUIRED
Content-Type: application/json

Request Body:
{
  "groupId": "550e8400-e29b-41d4-a716-446655440010",
  "title": "Question about Quantum Mechanics",
  "content": "I don't understand superposition. Can anyone help?"
}

Response: 201 Created
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "groupId": "550e8400-e29b-41d4-a716-446655440010",
  "userId": "your-user-id",
  "title": "Question about Quantum Mechanics",
  "content": "I don't understand superposition. Can anyone help?",
  "createdAt": "2026-05-10T10:30:00"
}

Error Responses:
- 401 Unauthorized: Missing token
- 400 Bad Request: Missing required fields
```

### Get All Materials (Public)
```
GET /api/materials?page=0&size=10
Authorization: NOT REQUIRED ✓

Response: 200 OK
{
  "content": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "groupId": "550e8400-e29b-41d4-a716-446655440010",
      "title": "Quantum Mechanics Lecture Notes",
      "url": "https://example.com/lecture-notes.pdf",
      "uploadedAt": "2026-05-10T10:30:00"
    }
  ],
  "totalElements": 50,
  "totalPages": 5,
  "currentPage": 0
}
```

### Upload Material (Protected)
```
POST /api/materials
Authorization: Bearer {accessToken} ✓ REQUIRED
Content-Type: multipart/form-data

Form Fields:
- file: (binary file)
- groupId: "550e8400-e29b-41d4-a716-446655440010"
- title: "Chapter 5 - Quantum Computing"

Response: 201 Created
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "groupId": "550e8400-e29b-41d4-a716-446655440010",
  "title": "Chapter 5 - Quantum Computing",
  "url": "https://storage.example.com/materials/...",
  "uploadedAt": "2026-05-10T10:30:00"
}

Error Responses:
- 401 Unauthorized: Missing token
- 413 Payload Too Large: File too large
- 400 Bad Request: Missing fields
```

## 👤 User Endpoints

### Get All Users (Public)
```
GET /api/users?page=0&size=10
Authorization: NOT REQUIRED ✓

Response: 200 OK
{
  "content": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440001",
      "email": "john@example.com",
      "role": "STUDENT",
      "createdAt": "2026-05-10T10:30:00"
    }
  ],
  "totalElements": 100,
  "totalPages": 10
}
```

### Get User Profile (Public)
```
GET /api/users/{userId}
Authorization: NOT REQUIRED ✓

Response: 200 OK
{
  "id": "550e8400-e29b-41d4-a716-446655440001",
  "email": "john@example.com",
  "role": "STUDENT",
  "profile": {
    "name": "John Doe",
    "bio": "Physics enthusiast",
    "interests": ["Physics", "Quantum Mechanics"]
  },
  "createdAt": "2026-05-10T10:30:00"
}

Error Responses:
- 404 Not Found: User doesn't exist
```

### Update User Profile (Protected - Own profile only)
```
PUT /api/users/{userId}
Authorization: Bearer {accessToken} ✓ REQUIRED
Content-Type: application/json

Request Body:
{
  "name": "John Doe",
  "bio": "Physics enthusiast & ML researcher",
  "interests": ["Physics", "Machine Learning"]
}

Response: 200 OK
{
  "id": "550e8400-e29b-41d4-a716-446655440001",
  "email": "john@example.com",
  "profile": {
    "name": "John Doe",
    "bio": "Physics enthusiast & ML researcher",
    "interests": ["Physics", "Machine Learning"]
  }
}

Error Responses:
- 401 Unauthorized: Missing token
- 403 Forbidden: Trying to update another user's profile
- 404 Not Found: User doesn't exist
```

## 🔗 Join Requests

### Create Join Request (Protected)
```
POST /api/groups/{groupId}/join-requests
Authorization: Bearer {accessToken} ✓ REQUIRED

Response: 201 Created
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "groupId": "550e8400-e29b-41d4-a716-446655440010",
  "userId": "your-user-id",
  "status": "PENDING",
  "requestedAt": "2026-05-10T10:30:00"
}

Error Responses:
- 401 Unauthorized: Missing token
- 404 Not Found: Group doesn't exist
- 409 Conflict: Already a member or already requested
```

### Get Join Requests (Protected - Group Creator)
```
GET /api/groups/{groupId}/join-requests
Authorization: Bearer {accessToken} ✓ REQUIRED

Response: 200 OK
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "userId": "550e8400-e29b-41d4-a716-446655440001",
    "status": "PENDING",
    "requestedAt": "2026-05-10T10:30:00"
  }
]

Error Responses:
- 401 Unauthorized: Missing token
- 403 Forbidden: Not the group creator
```

### Approve Join Request (Protected - Group Creator)
```
PUT /api/groups/{groupId}/join-requests/{requestId}/approve
Authorization: Bearer {accessToken} ✓ REQUIRED

Response: 200 OK

Error Responses:
- 401 Unauthorized: Missing token
- 403 Forbidden: Not authorized
```

## 📊 Header Information

All requests include auto-added headers by API Gateway:

```
X-User-Id: {userId}        # Auto-added when authenticated
X-User-Role: {role}        # Auto-added when authenticated
Authorization: Bearer {accessToken}  # Must be included for protected endpoints
Content-Type: application/json      # Include for JSON requests
```

## 🔐 Security Rules

| Role | Can Do |
|------|--------|
| STUDENT | Read public data, Create posts, Join groups, Update own profile |
| CREATOR | All STUDENT permissions + Create/Edit/Delete own groups |
| ADMIN | All permissions + Approve/Reject groups + Manage users |

## ⚡ Status Codes Cheat Sheet

| Code | Meaning | Action |
|------|---------|--------|
| 200 | OK | Request succeeded |
| 201 | Created | Resource created successfully |
| 204 | No Content | Successful operation, no response body |
| 400 | Bad Request | Check request data/parameters |
| 401 | Unauthorized | Login or refresh token |
| 403 | Forbidden | Check user role/permissions |
| 404 | Not Found | Resource doesn't exist |
| 409 | Conflict | Data conflict (e.g., duplicate email) |
| 500 | Server Error | Contact support |

---

**Last Updated**: May 10, 2026
**API Version**: 1.0
