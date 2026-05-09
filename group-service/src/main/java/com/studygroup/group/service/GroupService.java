package com.studygroup.group.service;

import com.studygroup.group.dto.CreateGroupRequest;
import com.studygroup.group.dto.GroupDto;
import com.studygroup.group.kafka.GroupEventProducer;
import com.studygroup.group.model.Group;
import com.studygroup.group.repository.GroupRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupEventProducer groupEventProducer;

    public GroupService(GroupRepository groupRepository, GroupEventProducer groupEventProducer) {
        this.groupRepository = groupRepository;
        this.groupEventProducer = groupEventProducer;
    }

    public Page<GroupDto> getAllGroups(Pageable pageable) {
        return groupRepository.findByStatus("APPROVED", pageable)
                .map(this::convertToDto);
    }

    public GroupDto getGroup(UUID id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        return convertToDto(group);
    }

    public List<GroupDto> searchGroups(String query, String subject, String location) {
        List<Group> groups;
        
        if (query != null && !query.trim().isEmpty()) {
            groups = groupRepository.searchByNameOrDescription(query.trim(), "APPROVED");
        } else if (subject != null && !subject.trim().isEmpty()) {
            groups = groupRepository.findBySubjectAndStatus(subject.trim(), "APPROVED");
        } else if (location != null && !location.trim().isEmpty()) {
            groups = groupRepository.findByLocationAndStatus(location.trim(), "APPROVED");
        } else {
            groups = groupRepository.findByStatus("APPROVED");
        }
        
        return groups.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public GroupDto createGroup(CreateGroupRequest request, UUID creatorId) {
        Group group = new Group();
        group.setId(UUID.randomUUID());
        group.setName(request.getName());
        group.setDescription(request.getDescription());
        group.setSubject(request.getSubject());
        group.setLocation(request.getLocation());
        group.setMeetingType(request.getMeetingType());
        group.setMeetingSchedule(request.getMeetingSchedule());
        group.setMaxMembers(request.getMaxMembers());
        group.setCreatorId(creatorId);
        group.setStatus("PENDING");
        group.setCreatedAt(LocalDateTime.now());

        group = groupRepository.save(group);
        
        // Publish group created event
        groupEventProducer.publishGroupCreated(group.getId(), creatorId);

        return convertToDto(group);
    }

    public GroupDto updateGroup(UUID id, CreateGroupRequest request) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        group.setName(request.getName());
        group.setDescription(request.getDescription());
        group.setSubject(request.getSubject());
        group.setLocation(request.getLocation());
        group.setMeetingType(request.getMeetingType());
        group.setMeetingSchedule(request.getMeetingSchedule());
        group.setMaxMembers(request.getMaxMembers());

        group = groupRepository.save(group);
        return convertToDto(group);
    }

    public void deleteGroup(UUID id) {
        groupRepository.deleteById(id);
    }

    public void approveGroup(UUID id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        
        group.setStatus("APPROVED");
        groupRepository.save(group);
    }

    public void rejectGroup(UUID id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        
        group.setStatus("REJECTED");
        groupRepository.save(group);
    }

    public List<UUID> getGroupMembers(UUID id) {
        return groupRepository.findGroupMembers(id);
    }

    private GroupDto convertToDto(Group group) {
        GroupDto dto = new GroupDto();
        dto.setId(group.getId());
        dto.setName(group.getName());
        dto.setDescription(group.getDescription());
        dto.setSubject(group.getSubject());
        dto.setLocation(group.getLocation());
        dto.setMeetingType(group.getMeetingType());
        dto.setMeetingSchedule(group.getMeetingSchedule());
        dto.setMaxMembers(group.getMaxMembers());
        dto.setCreatorId(group.getCreatorId());
        dto.setStatus(group.getStatus());
        dto.setCreatedAt(group.getCreatedAt());
        return dto;
    }
}
