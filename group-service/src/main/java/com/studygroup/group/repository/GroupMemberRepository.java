package com.studygroup.group.repository;

import com.studygroup.group.model.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, UUID> {

    boolean existsByGroupIdAndUserId(UUID groupId, UUID userId);

    GroupMember findByGroupIdAndUserId(UUID groupId, UUID userId);
}
