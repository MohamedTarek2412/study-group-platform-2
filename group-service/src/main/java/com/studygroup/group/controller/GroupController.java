package com.studygroup.group.controller;

import com.studygroup.group.dto.CreateGroupRequest;
import com.studygroup.group.dto.GroupDto;
import com.studygroup.group.dto.PageResponse;
import com.studygroup.group.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<GroupDto>> getAllGroups(
            Pageable pageable,
            @RequestHeader(value = "X-User-Role", required = false) String role) {
        if ("ADMIN".equals(role)) {
            return ResponseEntity.ok(PageResponse.from(groupService.getAllGroupsForAdmin(pageable)));
        }
        return ResponseEntity.ok(PageResponse.from(groupService.getAllGroups(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDto> getGroup(@PathVariable UUID id) {
        return ResponseEntity.ok(groupService.getGroup(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<GroupDto>> searchGroups(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String meetingSchedule) {
        return ResponseEntity.ok(groupService.searchGroups(q, subject, location, meetingSchedule));
    }

    @GetMapping("/creator/me")
    @PreAuthorize("hasRole('CREATOR')")
    public ResponseEntity<List<GroupDto>> getMyCreatedGroups(@RequestHeader("X-User-Id") String creatorId) {
        return ResponseEntity.ok(groupService.getGroupsByCreator(UUID.fromString(creatorId)));
    }

    @PostMapping
    @PreAuthorize("hasRole('CREATOR')")
    public ResponseEntity<GroupDto> createGroup(
            @Valid @RequestBody CreateGroupRequest request,
            @RequestHeader("X-User-Id") String creatorId) {
        return ResponseEntity.ok(groupService.createGroup(request, UUID.fromString(creatorId)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CREATOR')")
    public ResponseEntity<GroupDto> updateGroup(
            @PathVariable UUID id,
            @Valid @RequestBody CreateGroupRequest request,
            @RequestHeader("X-User-Id") String currentUserId) {
        
        GroupDto group = groupService.getGroup(id);
        if (!group.getCreatorId().toString().equals(currentUserId)) {
            return ResponseEntity.status(403).build();
        }
        
        return ResponseEntity.ok(groupService.updateGroup(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CREATOR')")
    public ResponseEntity<Void> deleteGroup(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") String currentUserId) {
        
        GroupDto group = groupService.getGroup(id);
        if (!group.getCreatorId().toString().equals(currentUserId)) {
            return ResponseEntity.status(403).build();
        }
        
        groupService.deleteGroup(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> approveGroup(@PathVariable UUID id) {
        groupService.approveGroup(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> rejectGroup(@PathVariable UUID id) {
        groupService.rejectGroup(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<List<UUID>> getGroupMembers(@PathVariable UUID id) {
        return ResponseEntity.ok(groupService.getGroupMembers(id));
    }
}
