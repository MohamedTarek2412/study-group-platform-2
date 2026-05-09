package com.studygroup.group.repository;

import com.studygroup.group.model.JoinRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JoinRequestRepository extends JpaRepository<JoinRequest, UUID> {

    List<JoinRequest> findByGroupIdAndStatus(UUID groupId, String status);

    boolean existsByGroupIdAndStudentIdAndStatus(UUID groupId, UUID studentId, String status);

    JoinRequest findByGroupIdAndStudentId(UUID groupId, UUID studentId);
}
