package com.studygroup.discussion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public class MaterialDto {

    private UUID id;
    private UUID groupId;
    private UUID uploaderId;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @NotBlank(message = "File URL is required")
    private String fileUrl;

    private String fileType;

    private LocalDateTime createdAt;

    // Constructors
    public MaterialDto() {}

    public MaterialDto(UUID groupId, UUID uploaderId, String title, String fileUrl, String fileType) {
        this.groupId = groupId;
        this.uploaderId = uploaderId;
        this.title = title;
        this.fileUrl = fileUrl;
        this.fileType = fileType;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getGroupId() { return groupId; }
    public void setGroupId(UUID groupId) { this.groupId = groupId; }

    public UUID getUploaderId() { return uploaderId; }
    public void setUploaderId(UUID uploaderId) { this.uploaderId = uploaderId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
