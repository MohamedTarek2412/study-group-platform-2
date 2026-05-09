package com.studygroup.group.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "group_members")
public class GroupMember {

    @EmbeddedId
    private GroupMemberId id;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    // Constructors
    public GroupMember() {}

    public GroupMember(UUID groupId, UUID userId) {
        this.id = new GroupMemberId(groupId, userId);
    }

    // Getters and Setters
    public GroupMemberId getId() { return id; }
    public void setId(GroupMemberId id) { this.id = id; }

    public LocalDateTime getJoinedAt() { return joinedAt; }
    public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }

    @PrePersist
    public void prePersist() {
        this.joinedAt = LocalDateTime.now();
    }

    // Convenience methods
    public UUID getGroupId() { return id.getGroupId(); }
    public UUID getUserId() { return id.getUserId(); }
}
