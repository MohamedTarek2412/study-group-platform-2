package com.studygroup.auth.service;

import com.studygroup.auth.dto.AuthResponse;
import com.studygroup.auth.dto.AuthValidateResponse;
import com.studygroup.auth.dto.LoginRequest;
import com.studygroup.auth.dto.RegisterRequest;
import com.studygroup.auth.kafka.UserEventProducer;
import com.studygroup.auth.local.LocalUserProfileBootstrap;
import com.studygroup.auth.model.Role;
import com.studygroup.auth.model.User;
import com.studygroup.auth.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserEventProducer userEventProducer;
    private final LocalUserProfileBootstrap localUserProfileBootstrap;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                      AuthenticationManager authenticationManager, JwtService jwtService,
                      UserEventProducer userEventProducer,
                      @Autowired(required = false) LocalUserProfileBootstrap localUserProfileBootstrap) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userEventProducer = userEventProducer;
        this.localUserProfileBootstrap = localUserProfileBootstrap;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        
        // Use role from request, default to STUDENT if not provided
        Role role = Role.STUDENT;
        try {
            role = Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            role = Role.STUDENT;
        }
        user.setRole(role);
        user.setStatus("ACTIVE");
        user.setCreatedAt(LocalDateTime.now());

        user = userRepository.save(user);

        // Publish user registered event
        userEventProducer.publishUserRegistered(user.getId(), user.getEmail());
        if (localUserProfileBootstrap != null) {
            localUserProfileBootstrap.sync(user.getId(), user.getEmail());
        }

        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new AuthResponse(token, refreshToken, user.getId().toString(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new AuthResponse(token, refreshToken, user.getId().toString(), user.getRole().name());
    }

    public AuthResponse refresh(String refreshToken) {
        if (refreshToken == null || !refreshToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String token = refreshToken.substring(7);
        String email = jwtService.extractEmail(token);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String newToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        return new AuthResponse(newToken, newRefreshToken, user.getId().toString(), user.getRole().name());
    }

    public AuthValidateResponse validateSession(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid token");
        }

        String tokenValue = authorizationHeader.substring(7);
        Claims claims = jwtService.parseAccessTokenClaims(tokenValue);
        String uid = claims.get("userId", String.class);
        String role = claims.get("role", String.class);
        return new AuthValidateResponse(uid, role);
    }
}
