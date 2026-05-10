package com.studygroup.user.controller;

import com.studygroup.user.service.UserService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Used when profile {@code local} is active (no Kafka): creates profile after registration.
 */
@Profile("local")
@RestController
@RequestMapping("/api/users")
public class LocalProfileBootstrapController {

    private final UserService userService;

    public LocalProfileBootstrapController(UserService userService) {
        this.userService = userService;
    }

    public record BootstrapRequest(UUID userId, String email) {}

    @PostMapping("/bootstrap")
    public ResponseEntity<Void> bootstrap(@RequestBody BootstrapRequest req) {
        userService.createProfile(req.userId(), req.email());
        return ResponseEntity.ok().build();
    }
}
