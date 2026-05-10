package com.studygroup.user.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreatorApprovalProducer {

    private static final Logger logger = LoggerFactory.getLogger(CreatorApprovalProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    public CreatorApprovalProducer(@Autowired(required = false) KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishCreatorApproved(UUID userId) {
        send("creator-approved", userId, "CREATOR_APPROVED");
    }

    public void publishCreatorRejected(UUID userId) {
        send("creator-rejected", userId, "CREATOR_REJECTED");
    }

    private void send(String topic, UUID userId, String eventType) {
        if (kafkaTemplate == null) {
            return;
        }

        String message = String.format("{\"userId\":\"%s\",\"eventType\":\"%s\"}", userId, eventType);
        try {
            kafkaTemplate.send(topic, message).whenComplete((result, ex) -> {
                if (ex != null) {
                    logger.warn("Failed to publish {} for {}: {}", eventType, userId, ex.getMessage());
                }
            });
        } catch (Exception e) {
            logger.warn("Failed to enqueue {} for {}: {}", eventType, userId, e.getMessage());
        }
    }
}
