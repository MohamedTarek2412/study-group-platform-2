package com.studygroup.auth.config;

import com.studygroup.auth.model.Role;
import com.studygroup.auth.model.User;
import com.studygroup.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class DefaultAdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${studygroup.default-admin.email:admin@studygroup.com}")
    private String adminEmail;

    @Value("${studygroup.default-admin.password:admin123}")
    private String adminPassword;

    public DefaultAdminSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.existsByEmail(adminEmail)) {
            return;
        }

        User admin = new User();
        admin.setId(UUID.randomUUID());
        admin.setEmail(adminEmail);
        admin.setPasswordHash(passwordEncoder.encode(adminPassword));
        admin.setRole(Role.ADMIN);
        admin.setStatus("ACTIVE");
        admin.setCreatedAt(LocalDateTime.now());
        userRepository.save(admin);
    }
}
