package com.studygroup.group.controller;

import com.studygroup.group.dto.JoinRequestDto;
import com.studygroup.group.service.JoinRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/join-requests")
public class CreatorJoinRequestController {

    private final JoinRequestService joinRequestService;

    public CreatorJoinRequestController(JoinRequestService joinRequestService) {
        this.joinRequestService = joinRequestService;
    }

    @GetMapping("/creator/me")
    @PreAuthorize("hasRole('CREATOR')")
    public ResponseEntity<List<JoinRequestDto>> getMyJoinRequests(
            @RequestHeader("X-User-Id") String currentUserId) {
        return ResponseEntity.ok(joinRequestService.getJoinRequestsForCreator(UUID.fromString(currentUserId)));
    }
}
