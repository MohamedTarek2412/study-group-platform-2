package com.studygroup.auth.dto;

public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String userId;
    private String role;

    // Constructors
    public AuthResponse() {}

    public AuthResponse(String accessToken, String refreshToken, String userId, String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.role = role;
    }

    // Getters and Setters
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
