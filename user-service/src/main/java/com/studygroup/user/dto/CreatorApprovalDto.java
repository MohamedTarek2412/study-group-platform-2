package com.studygroup.user.dto;

import jakarta.validation.constraints.NotBlank;

public class CreatorApprovalDto {

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Action is required")
    private String action; // APPROVE or REJECT

    // Constructors
    public CreatorApprovalDto() {}

    public CreatorApprovalDto(String userId, String action) {
        this.userId = userId;
        this.action = action;
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
}
