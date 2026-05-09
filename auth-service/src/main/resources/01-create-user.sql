-- Create database and user for auth service
-- This script runs as postgres superuser
CREATE USER IF NOT EXISTS auth_user WITH PASSWORD 'auth_pass';
CREATE DATABASE IF NOT EXISTS auth_db OWNER auth_user;
GRANT ALL PRIVILEGES ON DATABASE auth_db TO auth_user;
