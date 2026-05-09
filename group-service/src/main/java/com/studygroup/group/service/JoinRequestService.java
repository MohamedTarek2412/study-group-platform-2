package com.studygroup.group.service;

import com.studygroup.group.dto.JoinRequestDto;
import com.studygroup.group.kafka.GroupEventProducer;
import com.studygroup.group.model.Group;
import com.studygroup.group.model.GroupMember;
import com.studygroup.group.model.JoinRequest;
import com.studygroup.group.repository.GroupMemberRepository;
import com.studygroup.group.repository.GroupRepository;
import com.studygroup.group.repository.JoinRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JoinRequestService {

    private final JoinRequestRepository joinRequestRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupEventProducer groupEventProducer;

    public JoinRequestService(JoinRequestRepository joinRequestRepository,
                            GroupRepository groupRepository,
                            GroupMemberRepository groupMemberRepository,
                            GroupEventProducer groupEventProducer) {
        this.joinRequestRepository = joinRequestRepository;
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.groupEventProducer = groupEventProducer;
    }

    public JoinRequestDto createJoinRequest(UUID groupId, UUID studentId) {
        // Check if group exists and is approved
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        
        if (!"APPROVED".equals(group.getStatus())) {
            throw new RuntimeException("Group is not available for joining");
        }

        // Check if user is already a member
        if (groupMemberRepository.existsByGroupIdAndUserId(groupId, studentId)) {
            throw new RuntimeException("User is already a member of this group");
        }

        // Check if there's already a pending request
        if (joinRequestRepository.existsByGroupIdAndStudentIdAndStatus(groupId, studentId, "PENDING")) {
            throw new RuntimeException("Join request already exists");
        }

        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setId(UUID.randomUUID());
        joinRequest.setGroupId(groupId);
        joinRequest.setStudentId(studentId);
        joinRequest.setStatus("PENDING");
        joinRequest.setCreatedAt(LocalDateTime.now());

        joinRequest = joinRequestRepository.save(joinRequest);

        // Publish join requested event
        groupEventProducer.publishJoinRequested(groupId, studentId);

        return convertToDto(joinRequest);
    }

    public List<JoinRequestDto> getJoinRequests(UUID groupId) {
        return joinRequestRepository.findByGroupIdAndStatus(groupId, "PENDING").stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void acceptJoinRequest(UUID requestId) {
        JoinRequest joinRequest = joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Join request not found"));

        joinRequest.setStatus("ACCEPTED");
        joinRequestRepository.save(joinRequest);

        // Add user to group members
        GroupMember groupMember = new GroupMember(joinRequest.getGroupId(), joinRequest.getStudentId());
        groupMemberRepository.save(groupMember);

        // Publish request approved event
        groupEventProducer.publishRequestApproved(joinRequest.getGroupId(), joinRequest.getStudentId());
    }

    public void rejectJoinRequest(UUID requestId) {
        JoinRequest joinRequest = joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Join request not found"));

        joinRequest.setStatus("REJECTED");
        joinRequestRepository.save(joinRequest);
    }

    public boolean isGroupCreator(UUID groupId, UUID userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        return group.getCreatorId().equals(userId);
    }

    private JoinRequestDto convertToDto(JoinRequest joinRequest) {
        JoinRequestDto dto = new JoinRequestDto();
        dto.setId(joinRequest.getId());
        dto.setGroupId(joinRequest.getGroupId());
        dto.setStudentId(joinRequest.getStudentId());
        dto.setStatus(joinRequest.getStatus());
        dto.setMessage(joinRequest.getMessage());
        dto.setCreatedAt(joinRequest.getCreatedAt());
        return dto;
    }
}
