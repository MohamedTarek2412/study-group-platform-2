package com.studygroup.group.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "join_requests",
       uniqueConstraints = @UniqueConstraint(columnNames = {"group_id", "student_id"}))
public class JoinRequest {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "group_id", nullable = false)
    private UUID groupId;

    @Column(name = "student_id", nullable = false)
    private UUID studentId;

    @Column(name = "status")
    private String status = "PENDING"; // PENDING, ACCEPTED, REJECTED

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructors
    public JoinRequest() {}

    public JoinRequest(UUID groupId, UUID studentId, String message) {
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

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
