package com.studygroup.auth.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public UserEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishUserRegistered(UUID userId, String email) {
        String message = String.format("{\"userId\":\"%s\",\"email\":\"%s\",\"eventType\":\"USER_REGISTERED\"}", 
                userId.toString(), email);
        kafkaTemplate.send("user-registered", message);
    }
}
