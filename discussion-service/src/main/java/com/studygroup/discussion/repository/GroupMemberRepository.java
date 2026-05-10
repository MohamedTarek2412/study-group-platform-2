package com.studygroup.discussion.repository;

import com.studygroup.discussion.model.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, UUID> {

    boolean existsByGroupIdAndUserId(UUID groupId, UUID userId);
}
