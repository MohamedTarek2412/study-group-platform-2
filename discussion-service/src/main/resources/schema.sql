-- Discussion Service Database Schema
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS posts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    group_id UUID NOT NULL,
    author_id UUID NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_posts_group ON posts(group_id);

CREATE TABLE IF NOT EXISTS replies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    post_id UUID NOT NULL,
    author_id UUID NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_replies_post ON replies(post_id);

CREATE TABLE IF NOT EXISTS materials (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    group_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    file_url VARCHAR(500),
    file_type VARCHAR(100),
    uploader_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_materials_group ON materials(group_id);
CREATE INDEX IF NOT EXISTS idx_materials_uploader ON materials(uploader_id);
CREATE INDEX IF NOT EXISTS idx_materials_created ON materials(created_at);

CREATE TABLE IF NOT EXISTS group_members (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    group_id UUID NOT NULL,
    user_id UUID NOT NULL,
    CONSTRAINT uk_group_members_group_user UNIQUE (group_id, user_id)
);

CREATE INDEX IF NOT EXISTS idx_group_members_group_user ON group_members(group_id, user_id);

CREATE TABLE IF NOT EXISTS group_creators (
    group_id UUID PRIMARY KEY,
    creator_id UUID NOT NULL
);
