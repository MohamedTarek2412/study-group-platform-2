package com.studygroup.group.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class JoinRequestDto {

    private UUID id;
    private UUID groupId;
    private UUID studentId;
    private String status; // PENDING, ACCEPTED, REJECTED
    private String message;
    private LocalDateTime createdAt;

    // Constructors
    public JoinRequestDto() {}

    public JoinRequestDto(UUID groupId, UUID studentId, String message) {
        this.groupId = groupId;
        this.studentId = studentId;
        this.message = message;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getGroupId() { return groupId; }
    public void setGroupId(UUID groupId) { this.groupId = groupId; }

    public UUID getStudentId() { return studentId; }
    public void setStudentId(UUID studentId) { this.studentId = studentId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
