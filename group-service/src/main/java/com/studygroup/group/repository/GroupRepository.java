package com.studygroup.group.repository;

import com.studygroup.group.model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<Group, UUID> {

    Page<Group> findByStatus(String status, Pageable pageable);

    List<Group> findBySubjectAndStatus(String subject, String status);

    List<Group> findByLocationAndStatus(String location, String status);

    List<Group> findByStatus(String status);

    @Query("SELECT g FROM Group g WHERE g.status = :status AND " +
           "(LOWER(g.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(g.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Group> searchByNameOrDescription(@Param("query") String query, @Param("status") String status);

    @Query("SELECT gm.userId FROM GroupMember gm WHERE gm.groupId = :groupId")
    List<UUID> findGroupMembers(@Param("groupId") UUID groupId);
}
