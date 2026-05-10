package com.studygroup.group.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GroupEventProducer {

    private static final Logger logger = LoggerFactory.getLogger(GroupEventProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    public GroupEventProducer(@Autowired(required = false) KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishGroupCreated(UUID groupId, UUID creatorId) {
        if (kafkaTemplate == null) {
            return;
        }
        String message = String.format("{\"groupId\":\"%s\",\"creatorId\":\"%s\",\"eventType\":\"GROUP_CREATED\"}",
                groupId.toString(), creatorId.toString());
        send("group-created", message);
    }

    public void publishJoinRequested(UUID groupId, UUID studentId) {
        if (kafkaTemplate == null) {
            return;
        }
        String message = String.format("{\"groupId\":\"%s\",\"studentId\":\"%s\",\"eventType\":\"JOIN_REQUESTED\"}",
                groupId.toString(), studentId.toString());
        send("join-requested", message);
    }

    public void publishRequestApproved(UUID groupId, UUID studentId) {
        if (kafkaTemplate == null) {
            return;
        }
        String message = String.format("{\"groupId\":\"%s\",\"studentId\":\"%s\",\"eventType\":\"REQUEST_APPROVED\"}",
                groupId.toString(), studentId.toString());
        send("request-approved", message);
    }

    private void send(String topic, String message) {
        try {
            kafkaTemplate.send(topic, message)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            logger.warn("Failed to publish event to {}: {}", topic, ex.getMessage());
                        }
                    });
        } catch (Exception e) {
            logger.warn("Failed to enqueue event to {}: {}", topic, e.getMessage());
        }
    }
}
