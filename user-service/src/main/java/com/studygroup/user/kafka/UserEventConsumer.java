package com.studygroup.user.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studygroup.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Profile("!local")
public class UserEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(UserEventConsumer.class);
    
    private final UserService userService;
    private final ObjectMapper objectMapper;

    public UserEventConsumer(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "user-registered", groupId = "user-service")
    public void handleUserRegistered(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            String userIdStr = jsonNode.get("userId").asText();
            String email = jsonNode.get("email").asText();
            String requestedRole = jsonNode.hasNonNull("requestedRole")
                    ? jsonNode.get("requestedRole").asText()
                    : "STUDENT";
            
            UUID userId = UUID.fromString(userIdStr);
            userService.createProfile(userId, email, requestedRole);
            
            logger.info("Created user profile for user: {}", userId);
        } catch (Exception e) {
            logger.error("Error processing user registered event: {}", e.getMessage(), e);
        }
    }
}
