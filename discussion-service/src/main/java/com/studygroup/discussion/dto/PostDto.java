package com.studygroup.discussion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PostDto {

    private UUID id;
    private UUID groupId;
    private UUID authorId;

    @NotBlank(message = "Content is required")
    @Size(max = 2000, message = "Content must not exceed 2000 characters")
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ReplyDto> replies;

    // Constructors
    public PostDto() {}

    public PostDto(UUID groupId, UUID authorId, String content) {
        this.groupId = groupId;
        this.authorId = authorId;
        this.content = content;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getGroupId() { return groupId; }
    public void setGroupId(UUID groupId) { this.groupId = groupId; }

    public UUID getAuthorId() { return authorId; }
    public void setAuthorId(UUID authorId) { this.authorId = authorId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<ReplyDto> getReplies() { return replies; }
    public void setReplies(List<ReplyDto> replies) { this.replies = replies; }

    // ReplyDto nested class
    public static class ReplyDto {
        private UUID id;
        private UUID postId;
        private UUID authorId;
        private String content;
        private LocalDateTime createdAt;

        // Constructors
        public ReplyDto() {}

        public ReplyDto(UUID postId, UUID authorId, String content) {
            this.postId = postId;
            this.authorId = authorId;
            this.content = content;
        }

        // Getters and Setters
        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }

        public UUID getPostId() { return postId; }
        public void setPostId(UUID postId) { this.postId = postId; }

        public UUID getAuthorId() { return authorId; }
        public void setAuthorId(UUID authorId) { this.authorId = authorId; }

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }

        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }
}
