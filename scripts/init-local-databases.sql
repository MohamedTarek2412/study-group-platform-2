-- Run once in psql as superuser: psql -U postgres -f scripts/init-local-databases.sql
CREATE DATABASE auth_db;
CREATE DATABASE user_db;
CREATE DATABASE group_db;
CREATE DATABASE discussion_db;
