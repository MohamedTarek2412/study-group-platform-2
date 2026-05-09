# Complete Verification Checklist

## ✅ Pre-Deployment Checklist

### Backend Configuration Files
- [ ] auth-service/src/main/java/com/studygroup/auth/config/SecurityConfig.java - Has CORS and permits public endpoints
- [ ] group-service/src/main/java/com/studygroup/group/config/SecurityConfig.java - Created with CORS ✅ NEW
- [ ] user-service/src/main/java/com/studygroup/user/config/SecurityConfig.java - Created with CORS ✅ NEW
- [ ] discussion-service/src/main/java/com/studygroup/discussion/config/SecurityConfig.java - Created with CORS ✅ NEW
- [ ] api-gateway/src/main/java/com/studygroup/gateway/config/SecurityConfig.java - Updated with Spring Security
- [ ] api-gateway/src/main/java/com/studygroup/gateway/filter/JwtAuthFilter.java - Updated isPublicEndpoint method

### Backend Filter Files
- [ ] group-service/src/main/java/com/studygroup/group/filter/HeaderAuthenticationFilter.java - Created ✅ NEW
- [ ] user-service/src/main/java/com/studygroup/user/filter/HeaderAuthenticationFilter.java - Created ✅ NEW
- [ ] discussion-service/src/main/java/com/studygroup/discussion/filter/HeaderAuthenticationFilter.java - Created ✅ NEW
- [ ] auth-service/src/main/java/com/studygroup/auth/filter/JwtAuthFilter.java - Unchanged, should work fine

### Frontend Files
- [ ] frontend/src/api/authApi.js - register() includes role parameter
- [ ] frontend/src/api/groupApi.js - Public GET endpoints
- [ ] frontend/src/components/auth/RegisterForm.jsx - Has role selector dropdown
- [ ] frontend/src/context/AuthContext.jsx - register() function has 3 parameters
- [ ] frontend/src/utils/axiosInstance.js - Properly configured

### Dependencies
- [ ] api-gateway/pom.xml - Has spring-boot-starter-security added
- [ ] auth-service/pom.xml - Has all required dependencies
- [ ] group-service/pom.xml - Has spring-boot-starter-security
- [ ] user-service/pom.xml - Has spring-boot-starter-security
- [ ] discussion-service/pom.xml - Has spring-boot-starter-security

## 🚀 Deployment Checklist

### Build Phase
```
[ ] mvn clean package succeeds for auth-service
[ ] mvn clean package succeeds for user-service
[ ] mvn clean package succeeds for group-service
[ ] mvn clean package succeeds for discussion-service
[ ] mvn clean package succeeds for api-gateway
[ ] npm install & npm run build succeeds for frontend
```

### Docker Phase
```
[ ] docker-compose up --build starts successfully
[ ] All 10 containers are running (use: docker ps)
[ ] No critical errors in any container logs
```

### Container Status Check
```
[ ] zookeeper: healthy
[ ] kafka: healthy
[ ] auth-db: accepting connections
[ ] user-db: accepting connections
[ ] group-db: accepting connections
[ ] discussion-db: accepting connections
[ ] auth-service: running on port 8081
[ ] user-service: running on port 8082
[ ] group-service: running on port 8083
[ ] discussion-service: running on port 8084
[ ] api-gateway: running on port 8080
```

## 🔍 API Testing Checklist

### Public Endpoints (No Auth Required)
```
[ ] GET /api/auth/register returns 405 (POST only) ✓
[ ] POST /api/auth/register with valid data returns 200 + tokens
[ ] POST /api/auth/login with valid data returns 200 + tokens
[ ] GET /api/groups returns 200 + groups list
[ ] GET /api/groups/{id} returns 200 + group details
[ ] GET /api/groups/search returns 200 + search results
[ ] GET /api/users returns 200 (without auth)
[ ] GET /api/posts returns 200 (without auth)
[ ] GET /api/materials returns 200 (without auth)
```

### Protected Endpoints (Auth Required)
```
[ ] POST /api/groups (requires token + CREATOR role)
[ ] PUT /api/groups/{id} (requires token + CREATOR role)
[ ] DELETE /api/groups/{id} (requires token + CREATOR role)
[ ] POST /api/posts (requires token)
[ ] PUT /api/posts/{id} (requires token)
[ ] DELETE /api/posts/{id} (requires token)
```

### Token Management
```
[ ] accessToken saved in localStorage after login
[ ] refreshToken saved in localStorage after login
[ ] Authorization header includes "Bearer {token}"
[ ] 401 response triggers token refresh
[ ] Expired token auto-refreshes transparently
[ ] Failed refresh redirects to /login
```

## 🎯 Frontend Testing Checklist

### Registration Flow
```
[ ] Navigate to /register
[ ] Form has fields: email, password, confirmPassword, role
[ ] Role dropdown shows: STUDENT, CREATOR, ADMIN
[ ] Submit valid form → redirects to /dashboard
[ ] Check localStorage has accessToken and refreshToken
[ ] Check console shows 200 response on register API call
```

### Login Flow
```
[ ] Navigate to /login
[ ] Submit email and password
[ ] Redirects to /dashboard
[ ] Check localStorage has accessToken and refreshToken
[ ] User object in context has id and role
```

### Groups Page
```
[ ] Navigate to /groups (or /groups-page)
[ ] Page loads WITHOUT requiring login
[ ] Groups list displays (or shows "No groups" message)
[ ] Check network tab: GET /api/groups shows 200
[ ] No Authorization header needed for GET
```

### Protected Pages
```
[ ] After login, can access user dashboard
[ ] Can view personal information
[ ] Can create/edit/delete own content (based on role)
[ ] Cannot perform actions without proper role
[ ] Cannot access after logout
```

## 📊 Error Response Checklist

### Correct Responses
```
[ ] 200 OK: Successful GET request
[ ] 201 Created: Successful POST for resource creation
[ ] 204 No Content: Successful DELETE
[ ] 400 Bad Request: Invalid request data
[ ] 401 Unauthorized: Missing/invalid token
[ ] 403 Forbidden: Valid token but insufficient role
[ ] 404 Not Found: Resource doesn't exist
[ ] 409 Conflict: Email already exists (registration)
```

## 🐛 Debugging Commands

### Check Containers
```powershell
docker ps                                    # List running containers
docker logs <container-name>                 # View container logs
docker exec -it <container> /bin/bash        # Access container shell
```

### Test APIs Directly
```powershell
# Test registration
$body = @{email="test@test.com";password="pass123";role="STUDENT"} | ConvertTo-Json
Invoke-RestMethod -Uri "http://localhost:8080/api/auth/register" `
  -Method POST -ContentType "application/json" -Body $body

# Test get groups
Invoke-RestMethod -Uri "http://localhost:8080/api/groups" -Method GET
```

### Database Access
```powershell
# Connect to auth database
docker exec -it <auth-db-container> psql -U auth_user -d auth_db

# View users table
SELECT id, email, role, status FROM users;
```

## ✨ Success Indicators

If all the following are true, the system is working correctly:

1. ✅ User can register with email, password, and role selection
2. ✅ Registered user receives accessToken and refreshToken
3. ✅ User can login with registered credentials
4. ✅ User can view public groups without logging in
5. ✅ Logged-in user can create groups (if CREATOR role)
6. ✅ User can view their profile after login
7. ✅ Expired tokens are automatically refreshed
8. ✅ User is redirected to login when token refresh fails
9. ✅ CORS allows requests from localhost:3000
10. ✅ No "ERR_EMPTY_RESPONSE" or "403 Forbidden" errors

## 🔧 If Something Doesn't Work

1. **Check container logs**: `docker logs <service-name>`
2. **Verify network connectivity**: `docker network ls` and `docker network inspect studygroup-net`
3. **Check database**: Connect via pgAdmin or psql CLI
4. **Verify ports**: `netstat -an | findstr :8080` (Windows)
5. **Clear browser cache**: Ctrl+Shift+Delete
6. **Check browser console**: F12 → Console tab for JavaScript errors
7. **Review application.yml files**: Ensure database URLs are correct
8. **Verify JWT secret**: Same secret should be used in all services

## 📞 Common Issues & Quick Fixes

| Issue | Cause | Fix |
|-------|-------|-----|
| ERR_EMPTY_RESPONSE | Service not running | docker-compose up --build |
| 403 Forbidden | CORS issue | Check corsConfigurationSource() in SecurityConfig |
| 401 Unauthorized | Missing auth | Login first or check token in localStorage |
| Database error | Container not ready | Wait 30 seconds, try again |
| CORS preflight fails | Missing OPTIONS | Already configured in SecurityConfig |
| Token not stored | Registration failed | Check response status and error message |

---

**Last Updated**: May 10, 2026
**Version**: 1.0 - Complete Fix
