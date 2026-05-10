package com.studygroup.auth.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studygroup.auth.model.Role;
import com.studygroup.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Profile("!local")
public class CreatorApprovalConsumer {

    private static final Logger logger = LoggerFactory.getLogger(CreatorApprovalConsumer.class);

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public CreatorApprovalConsumer(UserRepository userRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "creator-approved", groupId = "auth-service")
    public void handleCreatorApproved(String message) {
        updateRole(message, Role.CREATOR);
    }

    @KafkaListener(topics = "creator-rejected", groupId = "auth-service")
    public void handleCreatorRejected(String message) {
        updateRole(message, Role.STUDENT);
    }

    private void updateRole(String message, Role role) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            UUID userId = UUID.fromString(jsonNode.get("userId").asText());
            userRepository.findById(userId).ifPresent(user -> {
                user.setRole(role);
                userRepository.save(user);
            });
        } catch (Exception e) {
            logger.error("Error processing creator approval event: {}", e.getMessage(), e);
        }
    }
}
