package com.studygroup.group.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "StudyGroup")
@Table(name = "groups")
public class Group {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "subject")
    private String subject;

    @Column(name = "location")
    private String location;

    @Column(name = "meeting_type")
    private String meetingType; // ONLINE or OFFLINE

    @Column(name = "meeting_schedule")
    private String meetingSchedule;

    @Column(name = "max_members")
    private Integer maxMembers = 50;

    @Column(name = "creator_id", nullable = false)
    private UUID creatorId;

    @Column(name = "status")
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED, ARCHIVED

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructors
    public Group() {}

    public Group(String name, String description, String subject, String location, 
                String meetingType, String meetingSchedule, Integer maxMembers, UUID creatorId) {
        this.name = name;
        this.description = description;
        this.subject = subject;
        this.location = location;
        this.meetingType = meetingType;
        this.meetingSchedule = meetingSchedule;
        this.maxMembers = maxMembers;
        this.creatorId = creatorId;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getMeetingType() { return meetingType; }
    public void setMeetingType(String meetingType) { this.meetingType = meetingType; }

    public String getMeetingSchedule() { return meetingSchedule; }
    public void setMeetingSchedule(String meetingSchedule) { this.meetingSchedule = meetingSchedule; }

    public Integer getMaxMembers() { return maxMembers; }
    public void setMaxMembers(Integer maxMembers) { this.maxMembers = maxMembers; }

    public UUID getCreatorId() { return creatorId; }
    public void setCreatorId(UUID creatorId) { this.creatorId = creatorId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
