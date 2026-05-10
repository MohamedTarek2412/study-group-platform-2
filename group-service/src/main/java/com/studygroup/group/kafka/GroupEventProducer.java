package com.studygroup.group.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GroupEventProducer {

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
        kafkaTemplate.send("group-created", message);
    }

    public void publishJoinRequested(UUID groupId, UUID studentId) {
        if (kafkaTemplate == null) {
            return;
        }
        String message = String.format("{\"groupId\":\"%s\",\"studentId\":\"%s\",\"eventType\":\"JOIN_REQUESTED\"}",
                groupId.toString(), studentId.toString());
        kafkaTemplate.send("join-requested", message);
    }

    public void publishRequestApproved(UUID groupId, UUID studentId) {
        if (kafkaTemplate == null) {
            return;
        }
        String message = String.format("{\"groupId\":\"%s\",\"studentId\":\"%s\",\"eventType\":\"REQUEST_APPROVED\"}",
                groupId.toString(), studentId.toString());
        kafkaTemplate.send("request-approved", message);
    }
}
