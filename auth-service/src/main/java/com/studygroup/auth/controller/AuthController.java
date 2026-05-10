package com.studygroup.auth.controller;

import com.studygroup.auth.dto.AuthResponse;
import com.studygroup.auth.dto.AuthValidateResponse;
import com.studygroup.auth.dto.LoginRequest;
import com.studygroup.auth.dto.RegisterRequest;
import com.studygroup.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestHeader("Authorization") String refreshToken) {
        return ResponseEntity.ok(authService.refresh(refreshToken));
    }

    @GetMapping("/validate")
    public ResponseEntity<AuthValidateResponse> validate(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(authService.validateSession(token));
    }
}
