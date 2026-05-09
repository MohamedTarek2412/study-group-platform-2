package com.studygroup.discussion.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "materials")
public class Material {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "group_id", nullable = false)
    private UUID groupId;

    @Column(name = "uploader_id", nullable = false)
    private UUID uploaderId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructors
    public Material() {}

    public Material(UUID groupId, UUID uploaderId, String title, String fileUrl, String fileType) {
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

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
