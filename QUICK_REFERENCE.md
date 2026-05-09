# ⚡ Quick Reference Card

## 🚀 Start Application in 2 Minutes

```powershell
# 1. From project root, build all services
docker-compose up --build

# 2. Open browser
http://localhost:3000
```

## 📝 Test Flows

### Register & Login
```
1. Go to http://localhost:3000/register
2. Enter: email, password, select role (STUDENT/CREATOR/ADMIN)
3. Register → Check token in localStorage (F12)
4. Login page → Use registered email/password
5. Success → See dashboard
```

### View Public Groups
```
1. Go to http://localhost:3000/groups
2. Should work WITHOUT login
3. Can also open: http://localhost:8080/api/groups in browser
```

### Create Group (if CREATOR)
```
1. Login with CREATOR role
2. Navigate to create group page
3. Fill: name, description, subject, location
4. Submit → Should succeed (201 Created)
```

## 🔑 Test Credentials

| Email | Password | Role |
|-------|----------|------|
| test@test.com | pass123 | STUDENT |
| creator@test.com | pass123 | CREATOR |
| admin@test.com | pass123 | ADMIN |

(Create these during testing via registration form)

## 🌐 Important URLs

| Service | URL |
|---------|-----|
| Frontend | http://localhost:3000 |
| API Gateway | http://localhost:8080 |
| Auth Service | http://localhost:8081 |
| User Service | http://localhost:8082 |
| Group Service | http://localhost:8083 |
| Discussion Service | http://localhost:8084 |
| PostgreSQL Auth | localhost:5432 |
| Kafka | localhost:9092 |

## 📡 Key API Endpoints

| Method | Endpoint | Auth | Role |
|--------|----------|------|------|
| POST | /api/auth/register | ❌ | - |
| POST | /api/auth/login | ❌ | - |
| GET | /api/groups | ❌ | - |
| POST | /api/groups | ✅ | CREATOR |
| GET | /api/posts | ❌ | - |
| POST | /api/posts | ✅ | ANY |

## 🐛 Quick Debug

```powershell
# Check containers running
docker ps

# View service logs
docker logs api-gateway
docker logs auth-service
docker logs group-service

# Rebuild specific service
cd group-service
mvn clean package -DskipTests

# Restart all
docker-compose restart
```

## 💾 Storage Location

- **Tokens**: Browser localStorage
- **Database**: Docker PostgreSQL volumes
- **Files**: Docker volumes
- **Logs**: `docker logs <container>`

## ✅ What Was Fixed

| Issue | Fix |
|-------|-----|
| ERR_EMPTY_RESPONSE | SecurityConfig updated |
| 403 Forbidden | CORS added to all services |
| 401 Unauthorized | SecurityConfig created for each service |
| Registration | Backend accepts role parameter |
| Role Selection | Frontend has dropdown selector |

## 📚 Documentation Files

- `README_SOLUTION.md` - Full solution summary
- `FIXES_GUIDE.md` - Detailed fix explanations
- `API_REFERENCE.md` - All endpoints documented
- `VERIFICATION_CHECKLIST.md` - Complete testing checklist
- `build-and-run.bat` - Windows build script
- `build-and-run.sh` - Linux/Mac build script

## 🔐 Role Permissions

```
STUDENT:
- Read everything
- Create posts/materials
- Join groups

CREATOR:
- All STUDENT permissions
- Create/Edit/Delete own groups
- Manage group members

ADMIN:
- All permissions
- Approve/reject groups
- Manage all users
```

## 🎯 Common Commands

```powershell
# Full rebuild from scratch
docker-compose down
docker volume prune
docker-compose up --build

# View specific service logs
docker logs -f api-gateway

# Execute command in container
docker exec -it study-group-platform-auth-db-1 psql -U auth_user -d auth_db

# Stop everything
docker-compose down

# Start services only
docker-compose up
```

## ⚠️ If Something Goes Wrong

1. Check Docker status: `docker ps`
2. View logs: `docker logs <service>`
3. Rebuild: `docker-compose up --build`
4. Clear cache: Ctrl+Shift+Delete in browser
5. Hard restart: `docker-compose down && docker-compose up --build`

## ✨ Expected Behavior

✅ Register with email/password/role → Gets tokens
✅ Login with credentials → Gets tokens
✅ View public groups → Works without login
✅ Create group (if CREATOR) → Returns 201
✅ Access dashboard after login → Redirects properly
✅ Token refresh on 401 → Happens automatically
✅ Logout → Clears tokens and redirects

---

**Generated**: May 10, 2026
**All Issues**: ✅ FIXED
**Status**: READY TO TEST
