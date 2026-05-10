package com.studygroup.discussion.repository;

import com.studygroup.discussion.model.GroupCreator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GroupCreatorRepository extends JpaRepository<GroupCreator, UUID> {

    boolean existsByGroupIdAndCreatorId(UUID groupId, UUID creatorId);
}
