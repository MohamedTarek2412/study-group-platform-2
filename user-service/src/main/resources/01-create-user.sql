-- Create database and user for group service
-- This script runs as postgres superuser
CREATE USER IF NOT EXISTS group_user WITH PASSWORD 'group_pass';
CREATE DATABASE IF NOT EXISTS group_db OWNER group_user;
GRANT ALL PRIVILEGES ON DATABASE group_db TO group_user;
