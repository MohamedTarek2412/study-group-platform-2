package com.studygroup.discussion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "group_creators")
public class GroupCreator {

    @Id
    @Column(name = "group_id")
    private UUID groupId;

    @Column(name = "creator_id", nullable = false)
    private UUID creatorId;

    public GroupCreator() {}

    public GroupCreator(UUID groupId, UUID creatorId) {
        this.groupId = groupId;
        this.creatorId = creatorId;
    }

    public UUID getGroupId() { return groupId; }
    public void setGroupId(UUID groupId) { this.groupId = groupId; }

    public UUID getCreatorId() { return creatorId; }
    public void setCreatorId(UUID creatorId) { this.creatorId = creatorId; }
}
