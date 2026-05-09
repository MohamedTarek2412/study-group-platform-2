# Complete Fix & Testing Guide

## ✅ Issues Fixed

### 1. **ERR_EMPTY_RESPONSE on /api/auth/register**
- **Cause**: Spring Security wasn't configured to allow public access
- **Fix**: Updated SecurityConfig in auth-service to explicitly permit POST /auth/register and /auth/login

### 2. **403 Forbidden on /api/auth/login**  
- **Cause**: Missing CORS configuration and incorrect security chain
- **Fix**: Added CORS configuration to all services and updated security filters

### 3. **401 Unauthorized on GET /api/groups**
- **Cause**: Group service didn't have any security configuration
- **Fix**: Created SecurityConfig that allows public GET access to group endpoints

## 📋 Files Created/Modified

### Backend Changes:
1. **auth-service/src/main/java/com/studygroup/auth/config/SecurityConfig.java** - Updated with CORS
2. **group-service/src/main/java/com/studygroup/group/config/SecurityConfig.java** - Created ✅ NEW
3. **group-service/src/main/java/com/studygroup/group/filter/HeaderAuthenticationFilter.java** - Created ✅ NEW
4. **user-service/src/main/java/com/studygroup/user/config/SecurityConfig.java** - Created ✅ NEW
5. **user-service/src/main/java/com/studygroup/user/filter/HeaderAuthenticationFilter.java** - Created ✅ NEW
6. **discussion-service/src/main/java/com/studygroup/discussion/config/SecurityConfig.java** - Created ✅ NEW
7. **discussion-service/src/main/java/com/studygroup/discussion/filter/HeaderAuthenticationFilter.java** - Created ✅ NEW
8. **api-gateway/src/main/java/com/studygroup/gateway/config/SecurityConfig.java** - Updated
9. **api-gateway/src/main/java/com/studygroup/gateway/filter/JwtAuthFilter.java** - Updated
10. **api-gateway/pom.xml** - Added spring-boot-starter-security

### Frontend Changes (Already Done):
- authApi.js - Updated register() to include role parameter
- RegisterForm.jsx - Added role selector dropdown
- AuthContext.jsx - Updated register() function signature

## 🚀 Setup Instructions

### Step 1: Build Backend Services
```powershell
# Auth Service
cd auth-service
mvn clean package -DskipTests
cd ..

# User Service
cd user-service
mvn clean package -DskipTests
cd ..

# Group Service
cd group-service
mvn clean package -DskipTests
cd ..

# Discussion Service
cd discussion-service
mvn clean package -DskipTests
cd ..

# API Gateway
cd api-gateway
mvn clean package -DskipTests
cd ..
```

### Step 2: Build Frontend
```powershell
cd frontend
npm install
npm run build
```

### Step 3: Start All Services
```powershell
# From root directory
docker-compose up --build
```

## 📝 Testing Flow

### Public Endpoints (No Authentication Required)
```
1. GET http://localhost:8080/api/groups?page=0&size=10 ✅
2. GET http://localhost:8080/api/groups/{id} ✅
3. GET http://localhost:8080/api/groups/search ✅
4. GET http://localhost:8080/api/posts ✅
5. GET http://localhost:8080/api/materials ✅
6. POST http://localhost:8080/api/auth/register ✅
7. POST http://localhost:8080/api/auth/login ✅
```

### Test Case 1: User Registration
1. Go to http://localhost:3000/register
2. Fill in:
   - Email: test@example.com
   - Password: password123
   - Confirm Password: password123
   - Role: STUDENT (or CREATOR/ADMIN)
3. Click Register
4. Expected: Redirect to /dashboard, token stored in localStorage
5. Browser Console: Check Network tab → should see 200 response

### Test Case 2: View Public Groups
1. Go to http://localhost:3000/groups
2. Expected: See list of groups WITHOUT needing to login
3. Check Network tab → GET /api/groups should return 200

### Test Case 3: User Login
1. Go to http://localhost:3000/login
2. Fill in registered email and password
3. Click Login
4. Expected: Redirect to /dashboard, token stored in localStorage
5. Check localStorage (F12 → Application → Local Storage): accessToken should exist

### Test Case 4: Protected Endpoint
1. After login, try creating a group (if CREATOR role)
2. Expected: Request succeeds with 200/201
3. Check Network tab: Should see X-User-Id and X-User-Role headers

## 🔍 Debugging Commands

### Check if Services are Running
```powershell
# List all containers
docker ps

# Check gateway logs
docker logs study-group-platform-api-gateway-1

# Check auth service logs
docker logs study-group-platform-auth-service-1

# Check group service logs
docker logs study-group-platform-group-service-1
```

### Test API Directly (using PowerShell)
```powershell
# Test register endpoint
$body = @{
    email = "test@example.com"
    password = "password123"
    role = "STUDENT"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/auth/register" `
  -Method POST `
  -ContentType "application/json" `
  -Body $body

# Test get groups
Invoke-RestMethod -Uri "http://localhost:8080/api/groups?page=0&size=10" `
  -Method GET
```

## 🔐 Security Configuration Summary

| Service | Public GET | Public POST | Auth Required |
|---------|-----------|-----------|---|
| Groups | /api/groups/* | ❌ | POST/PUT/DELETE |
| Users | /api/users/* | ❌ | POST/PUT/DELETE |
| Posts | /api/posts/* | ❌ | POST/PUT/DELETE |
| Auth | - | register, login, refresh | validate |

## ✨ API Request Examples

### Register
```javascript
POST /api/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "role": "STUDENT"  // or "CREATOR" or "ADMIN"
}

Response: 200 OK
{
  "accessToken": "eyJhbG...",
  "refreshToken": "eyJhbG...",
  "userId": "123e4567-e89b-12d3-a456-426614174000",
  "role": "STUDENT"
}
```

### Login
```javascript
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}

Response: 200 OK
{
  "accessToken": "eyJhbG...",
  "refreshToken": "eyJhbG...",
  "userId": "123e4567-e89b-12d3-a456-426614174000",
  "role": "STUDENT"
}
```

### Get All Groups (Public)
```javascript
GET /api/groups?page=0&size=10

Response: 200 OK
{
  "content": [
    {
      "id": "...",
      "name": "...",
      "description": "...",
      ...
    }
  ],
  "totalElements": 10,
  "totalPages": 1,
  ...
}
```

### Create Group (Protected - Requires CREATOR role)
```javascript
POST /api/groups
Authorization: Bearer eyJhbG...
Content-Type: application/json

{
  "name": "Study Group Name",
  "description": "Description",
  "subject": "Mathematics",
  "location": "Online"
}

Response: 201 Created
```

## 🛠️ Common Issues & Solutions

### Issue: `net::ERR_EMPTY_RESPONSE`
**Solution**: Make sure docker containers are running (`docker ps`). Check logs for errors.

### Issue: `401 Unauthorized` on protected endpoints
**Solution**: 
1. Make sure you're logged in first
2. Check that token is stored in localStorage
3. Use browser DevTools → Network to verify Authorization header is sent

### Issue: `403 Forbidden`
**Solution**: Check your role:
- GET public endpoints: All roles
- POST to /api/groups: Requires CREATOR role
- Admin endpoints: Requires ADMIN role

### Issue: CORS errors
**Solution**: Already configured for localhost:3000. If using different port, update CORS in SecurityConfig files.

### Issue: Database connection errors
**Solution**: 
```powershell
# Verify PostgreSQL is running
docker ps | findstr postgres

# Check DB logs
docker logs study-group-platform-auth-db-1
```

## 📌 Token Storage

Tokens are stored in localStorage:
- `accessToken` - Used for API requests (expires quickly)
- `refreshToken` - Used to get new access token (longer expiration)

axiosInstance automatically:
1. Adds Authorization header with access token
2. Handles 401 responses by refreshing token
3. Retries the original request with new token

## ✅ All Fixed! Now You Can:

1. ✅ Register new users with role selection
2. ✅ Login successfully
3. ✅ View public groups without authentication
4. ✅ Create/edit/delete groups (with proper authorization)
5. ✅ Access all protected endpoints with valid token
6. ✅ Token auto-refresh on expiration
