package com.studygroup.group.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class CreateGroupRequest {

    @NotBlank(message = "Group name is required")
    @Size(max = 255, message = "Group name must not exceed 255 characters")
    private String name;

    private String description;

    @NotBlank(message = "Subject is required")
    private String subject;

    private String location;

    @NotBlank(message = "Meeting type is required")
    private String meetingType; // ONLINE or OFFLINE

    private String meetingSchedule;

    @Min(value = 2, message = "Maximum members must be at least 2")
    @Max(value = 100, message = "Maximum members must not exceed 100")
    private Integer maxMembers = 50;

    // Constructors
    public CreateGroupRequest() {}

    public CreateGroupRequest(String name, String description, String subject, String location,
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
}
