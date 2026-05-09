package com.studygroup.group.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public class GroupDto {

    private UUID id;

    @NotBlank(message = "Group name is required")
    @Size(max = 255, message = "Group name must not exceed 255 characters")
    private String name;

    private String description;

    private String subject;

    private String location;

    private String meetingType; // ONLINE or OFFLINE

    private String meetingSchedule;

    private Integer maxMembers = 50;

    private UUID creatorId;

    private String status; // PENDING, APPROVED, REJECTED, ARCHIVED

    private LocalDateTime createdAt;

    // Constructors
    public GroupDto() {}

    public GroupDto(String name, String description, String subject, String location,
                   String meetingType, String meetingSchedule, Integer maxMembers) {
        this.name = name;
        this.description = description;
        this.subject = subject;
        this.location = location;
        this.meetingType = meetingType;
        this.meetingSchedule = meetingSchedule;
        this.maxMembers = maxMembers;
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
}
