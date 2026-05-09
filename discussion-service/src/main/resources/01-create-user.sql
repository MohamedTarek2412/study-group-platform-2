-- Create database and user for discussion service
CREATE DATABASE IF NOT EXISTS discussion_db;
CREATE USER IF NOT EXISTS disc_user WITH PASSWORD 'disc_pass';
GRANT ALL PRIVILEGES ON DATABASE discussion_db TO disc_user;
