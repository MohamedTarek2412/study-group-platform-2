# Study Group Platform — run all backends locally WITHOUT Docker (Docker Desktop broken / I/O errors).
#
# Prerequisites:
#   1) JDK 21+ on PATH
#   2) PostgreSQL running on localhost:5432 — create DBs: run scripts\init-local-databases.sql (as postgres)
#   3) Default login in application-local.yml: postgres / postgres  (override with LOCAL_DB_USER / LOCAL_DB_PASSWORD)
#
# Optional: same JWT across services — already set in application-local.yml files.
#
# Start order matters: user-service must be up before you REGISTER (auth calls /api/users/bootstrap).
# Open 5 terminals and run each line in order (wait ~15s between starts):

$env:SPRING_PROFILES_ACTIVE = 'local'
$root = Split-Path -Parent $PSScriptRoot
Set-Location $root

Write-Host @"

=== Terminal 1 — user-service (8082) ===
cd `"$root\user-service`"
.\mvnw.cmd spring-boot:run `-Dspring-boot.run.profiles=local

=== Terminal 2 — auth-service (8081) ===
cd `"$root\auth-service`"
.\mvnw.cmd spring-boot:run `-Dspring-boot.run.profiles=local

=== Terminal 3 — group-service (8083) ===
cd `"$root\group-service`"
.\mvnw.cmd spring-boot:run `-Dspring-boot.run.profiles=local

=== Terminal 4 — discussion-service (8084) ===
cd `"$root\discussion-service`"
.\mvnw.cmd spring-boot:run `-Dspring-boot.run.profiles=local

=== Terminal 5 — api-gateway (8080) ===
cd `"$root\api-gateway`"
.\mvnw.cmd spring-boot:run `-Dspring-boot.run.profiles=local

=== Frontend (optional) ===
cd `"$root\frontend`"
npm install
npm start

Then open http://localhost:3000 — API base stays http://localhost:8080

"@
