package com.studygroup.group.controller;

import com.studygroup.group.dto.JoinRequestDto;
import com.studygroup.group.service.JoinRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/groups/{groupId}/join-requests")
public class JoinRequestController {

    private final JoinRequestService joinRequestService;

    public JoinRequestController(JoinRequestService joinRequestService) {
        this.joinRequestService = joinRequestService;
    }

    @PostMapping
    public ResponseEntity<JoinRequestDto> createJoinRequest(
            @PathVariable UUID groupId,
            @RequestHeader("X-User-Id") String studentId) {
        return ResponseEntity.ok(joinRequestService.createJoinRequest(groupId, UUID.fromString(studentId)));
    }

    @GetMapping
    @PreAuthorize("hasRole('CREATOR')")
    public ResponseEntity<List<JoinRequestDto>> getJoinRequests(
            @PathVariable UUID groupId,
            @RequestHeader("X-User-Id") String currentUserId) {
        
        // Verify the user is the creator of the group
        if (!joinRequestService.isGroupCreator(groupId, UUID.fromString(currentUserId))) {
            return ResponseEntity.status(403).build();
        }
        
        return ResponseEntity.ok(joinRequestService.getJoinRequests(groupId));
    }

    @PutMapping("/{requestId}/accept")
    @PreAuthorize("hasRole('CREATOR')")
    public ResponseEntity<Void> acceptJoinRequest(
            @PathVariable UUID groupId,
            @PathVariable UUID requestId,
            @RequestHeader("X-User-Id") String currentUserId) {
        
        if (!joinRequestService.isGroupCreator(groupId, UUID.fromString(currentUserId))) {
            return ResponseEntity.status(403).build();
        }
        
        joinRequestService.acceptJoinRequest(requestId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{requestId}/reject")
    @PreAuthorize("hasRole('CREATOR')")
    public ResponseEntity<Void> rejectJoinRequest(
            @PathVariable UUID groupId,
            @PathVariable UUID requestId,
            @RequestHeader("X-User-Id") String currentUserId) {
        
        if (!joinRequestService.isGroupCreator(groupId, UUID.fromString(currentUserId))) {
            return ResponseEntity.status(403).build();
        }
        
        joinRequestService.rejectJoinRequest(requestId);
        return ResponseEntity.ok().build();
    }
}
