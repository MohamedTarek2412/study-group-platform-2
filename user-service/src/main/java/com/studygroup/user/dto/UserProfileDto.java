package com.studygroup.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserProfileDto {

    private UUID id;
    private UUID userId;
    private String fullName;
    private String bio;
    private String avatarUrl;
    private String creatorStatus;
    private LocalDateTime updatedAt;

    // Constructors
    public UserProfileDto() {}

    public UserProfileDto(UUID userId, String fullName, String bio, String avatarUrl, String creatorStatus) {
        this.userId = userId;
        this.fullName = fullName;
        this.bio = bio;
        this.avatarUrl = avatarUrl;
        this.creatorStatus = creatorStatus;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getCreatorStatus() { return creatorStatus; }
    public void setCreatorStatus(String creatorStatus) { this.creatorStatus = creatorStatus; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
