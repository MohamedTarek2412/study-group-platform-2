package com.studygroup.user.controller;

import com.studygroup.user.dto.CreatorApprovalDto;
import com.studygroup.user.dto.UserProfileDto;
import com.studygroup.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDto> getUserProfile(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserProfile(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfileDto> updateUserProfile(
            @PathVariable UUID id,
            @Valid @RequestBody UserProfileDto profileDto,
            @RequestHeader("X-User-Id") String currentUserId) {
        
        if (!id.toString().equals(currentUserId)) {
            return ResponseEntity.status(403).build();
        }
        
        return ResponseEntity.ok(userService.updateUserProfile(id, profileDto));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserProfileDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}/approve-creator")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> approveCreator(@PathVariable UUID id) {
        userService.approveCreator(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reject-creator")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> rejectCreator(@PathVariable UUID id) {
        userService.rejectCreator(id);
        return ResponseEntity.ok().build();
    }
}
