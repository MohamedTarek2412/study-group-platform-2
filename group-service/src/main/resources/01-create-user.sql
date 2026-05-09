-- Create database and user for group service
CREATE DATABASE IF NOT EXISTS group_db;
CREATE USER IF NOT EXISTS group_user WITH PASSWORD 'group_pass';
GRANT ALL PRIVILEGES ON DATABASE group_db TO group_user;
