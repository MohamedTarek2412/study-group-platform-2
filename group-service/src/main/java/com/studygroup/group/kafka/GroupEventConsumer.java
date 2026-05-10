package com.studygroup.group.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studygroup.group.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Profile("!local")
public class GroupEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(GroupEventConsumer.class);
    
    private final UserService userService;
    private final ObjectMapper objectMapper;

    public GroupEventConsumer(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "creator-approved", groupId = "group-service")
    public void handleCreatorApproved(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            String userIdStr = jsonNode.get("userId").asText();
            
            UUID userId = UUID.fromString(userIdStr);
            userService.notifyCreatorApproved(userId);
            
            logger.info("Processed creator approval for user: {}", userId);
        } catch (Exception e) {
            logger.error("Error processing creator approved event: {}", e.getMessage(), e);
        }
    }
}
