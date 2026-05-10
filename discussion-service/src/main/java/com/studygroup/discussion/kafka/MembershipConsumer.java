package com.studygroup.discussion.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studygroup.discussion.service.GroupMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Profile("!local")
public class MembershipConsumer {

    private static final Logger logger = LoggerFactory.getLogger(MembershipConsumer.class);
    
    private final GroupMemberService groupMemberService;
    private final ObjectMapper objectMapper;

    public MembershipConsumer(GroupMemberService groupMemberService, ObjectMapper objectMapper) {
        this.groupMemberService = groupMemberService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "request-approved", groupId = "discussion-service")
    public void handleRequestApproved(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            String groupIdStr = jsonNode.get("groupId").asText();
            String studentIdStr = jsonNode.get("studentId").asText();
            
            UUID groupId = UUID.fromString(groupIdStr);
            UUID studentId = UUID.fromString(studentIdStr);
            
            groupMemberService.addMemberToCache(groupId, studentId);
            
            logger.info("Added member to cache - Group: {}, User: {}", groupId, studentId);
        } catch (Exception e) {
            logger.error("Error processing request approved event: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "group-created", groupId = "discussion-service")
    public void handleGroupCreated(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            UUID groupId = UUID.fromString(jsonNode.get("groupId").asText());
            UUID creatorId = UUID.fromString(jsonNode.get("creatorId").asText());

            groupMemberService.addCreatorToCache(groupId, creatorId);

            logger.info("Added group creator to cache - Group: {}, Creator: {}", groupId, creatorId);
        } catch (Exception e) {
            logger.error("Error processing group created event: {}", e.getMessage(), e);
        }
    }
}
