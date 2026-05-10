package com.studygroup.auth.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public UserEventProducer(@Autowired(required = false) KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishUserRegistered(UUID userId, String email) {
        if (kafkaTemplate == null) {
            return;
        }
        String message = String.format("{\"userId\":\"%s\",\"email\":\"%s\",\"eventType\":\"USER_REGISTERED\"}",
                userId.toString(), email);
        kafkaTemplate.send("user-registered", message);
    }
}
