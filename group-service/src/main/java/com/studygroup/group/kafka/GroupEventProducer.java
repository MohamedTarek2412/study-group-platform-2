package com.studygroup.group.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GroupEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public GroupEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishGroupCreated(UUID groupId, UUID creatorId) {
        String message = String.format("{\"groupId\":\"%s\",\"creatorId\":\"%s\",\"eventType\":\"GROUP_CREATED\"}", 
                groupId.toString(), creatorId.toString());
        kafkaTemplate.send("group-created", message);
    }

    public void publishJoinRequested(UUID groupId, UUID studentId) {
        String message = String.format("{\"groupId\":\"%s\",\"studentId\":\"%s\",\"eventType\":\"JOIN_REQUESTED\"}", 
                groupId.toString(), studentId.toString());
        kafkaTemplate.send("join-requested", message);
    }

    public void publishRequestApproved(UUID groupId, UUID studentId) {
        String message = String.format("{\"groupId\":\"%s\",\"studentId\":\"%s\",\"eventType\":\"REQUEST_APPROVED\"}", 
                groupId.toString(), studentId.toString());
        kafkaTemplate.send("request-approved", message);
    }
}
