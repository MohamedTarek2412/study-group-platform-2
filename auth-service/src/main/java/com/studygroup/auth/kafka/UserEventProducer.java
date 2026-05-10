package com.studygroup.auth.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserEventProducer {

    private static final Logger logger = LoggerFactory.getLogger(UserEventProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    public UserEventProducer(@Autowired(required = false) KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishUserRegistered(UUID userId, String email, String requestedRole) {
        if (kafkaTemplate == null) {
            return;
        }
        String message = String.format("{\"userId\":\"%s\",\"email\":\"%s\",\"requestedRole\":\"%s\",\"eventType\":\"USER_REGISTERED\"}",
                userId.toString(), email, requestedRole);
        try {
            kafkaTemplate.send("user-registered", message)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            logger.warn("Failed to publish user registered event for {}: {}", userId, ex.getMessage());
                        }
                    });
        } catch (Exception e) {
            logger.warn("Failed to enqueue user registered event for {}: {}", userId, e.getMessage());
        }
    }
}
