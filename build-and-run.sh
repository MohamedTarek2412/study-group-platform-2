#!/bin/bash

# Study Group Platform - Build & Deploy Script
# This script builds all services and starts the platform

echo "================================================"
echo "Study Group Platform - Complete Setup"
echo "================================================"
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print success
success() {
    echo -e "${GREEN}✓ $1${NC}"
}

# Function to print error
error() {
    echo -e "${RED}✗ $1${NC}"
}

# Function to print warning
warning() {
    echo -e "${YELLOW}→ $1${NC}"
}

# Step 1: Check Docker
echo "Step 1: Checking Docker..."
if ! command -v docker &> /dev/null; then
    error "Docker is not installed"
    exit 1
fi
success "Docker is installed"

# Step 2: Check Docker Compose
echo ""
echo "Step 2: Checking Docker Compose..."
if ! command -v docker-compose &> /dev/null; then
    error "Docker Compose is not installed"
    exit 1
fi
success "Docker Compose is installed"

# Step 3: Build Services
echo ""
echo "Step 3: Building all services..."
warning "This may take 5-10 minutes..."

services=("auth-service" "user-service" "group-service" "discussion-service" "api-gateway")

for service in "${services[@]}"; do
    echo ""
    echo "Building $service..."
    cd "$service"
    if mvn clean package -DskipTests > /dev/null 2>&1; then
        success "$service built successfully"
    else
        error "Failed to build $service"
        exit 1
    fi
    cd ..
done

# Step 4: Build Frontend
echo ""
echo "Step 4: Building frontend..."
cd frontend
if npm install > /dev/null 2>&1 && npm run build > /dev/null 2>&1; then
    success "Frontend built successfully"
else
    error "Failed to build frontend"
    exit 1
fi
cd ..

# Step 5: Start Services
echo ""
echo "Step 5: Starting Docker containers..."
if docker-compose up --build > /dev/null 2>&1; then
    success "Docker Compose started successfully"
else
    error "Failed to start Docker Compose"
    exit 1
fi
