@echo off
REM Study Group Platform - Build & Deploy Script (Windows)

echo ================================================
echo Study Group Platform - Complete Setup
echo ================================================
echo.

REM Step 1: Check Docker
echo Step 1: Checking Docker...
where docker >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Docker is not installed
    pause
    exit /b 1
)
echo [OK] Docker is installed

REM Step 2: Check Docker Compose
echo.
echo Step 2: Checking Docker Compose...
where docker-compose >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Docker Compose is not installed
    pause
    exit /b 1
)
echo [OK] Docker Compose is installed

REM Step 3: Build Services
echo.
echo Step 3: Building all services...
echo NOTE: This may take 5-10 minutes...

setlocal enabledelayedexpansion
set services=auth-service user-service group-service discussion-service api-gateway

for %%s in (%services%) do (
    echo.
    echo Building %%s...
    cd %%s
    call mvn clean package -DskipTests
    if %ERRORLEVEL% NEQ 0 (
        echo ERROR: Failed to build %%s
        pause
        exit /b 1
    )
    echo [OK] %%s built successfully
    cd ..
)

REM Step 4: Build Frontend
echo.
echo Step 4: Building frontend...
cd frontend
call npm install
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Failed to install frontend dependencies
    pause
    exit /b 1
)
call npm run build
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Failed to build frontend
    pause
    exit /b 1
)
echo [OK] Frontend built successfully
cd ..

REM Step 5: Start Services
echo.
echo Step 5: Starting Docker containers...
call docker-compose up --build
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Failed to start Docker Compose
    pause
    exit /b 1
)

pause
