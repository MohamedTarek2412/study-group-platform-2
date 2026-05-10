package com.studygroup.discussion.service;

import com.studygroup.discussion.dto.MaterialDto;
import com.studygroup.discussion.model.Material;
import com.studygroup.discussion.repository.MaterialRepository;
import com.studygroup.discussion.repository.GroupMemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupMemberService groupMemberService;

    public MaterialService(MaterialRepository materialRepository,
                           GroupMemberRepository groupMemberRepository,
                           GroupMemberService groupMemberService) {
        this.materialRepository = materialRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.groupMemberService = groupMemberService;
    }

    public Page<MaterialDto> getMaterials(UUID groupId, Pageable pageable) {
        return materialRepository.findByGroupIdOrderByCreatedAtDesc(groupId, pageable)
                .map(this::convertToDto);
    }

    public MaterialDto getMaterial(UUID materialId) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Material not found"));
        return convertToDto(material);
    }

    public MaterialDto uploadMaterial(UUID groupId, MaterialDto materialDto, UUID uploaderId) {
        Material material = new Material();
        material.setId(UUID.randomUUID());
        material.setGroupId(groupId);
        material.setUploaderId(uploaderId);
        material.setTitle(materialDto.getTitle());
        material.setFileUrl(materialDto.getFileUrl());
        material.setFileType(materialDto.getFileType());
        material.setCreatedAt(LocalDateTime.now());

        material = materialRepository.save(material);
        return convertToDto(material);
    }

    public void deleteMaterial(UUID materialId) {
        materialRepository.deleteById(materialId);
    }

    public boolean isGroupMember(UUID groupId, UUID userId) {
        return groupMemberRepository.existsByGroupIdAndUserId(groupId, userId);
    }

    public boolean isGroupCreator(UUID groupId, UUID userId) {
        return groupMemberService.isCreator(groupId, userId);
    }

    private MaterialDto convertToDto(Material material) {
        MaterialDto dto = new MaterialDto();
        dto.setId(material.getId());
        dto.setGroupId(material.getGroupId());
        dto.setUploaderId(material.getUploaderId());
        dto.setTitle(material.getTitle());
        dto.setFileUrl(material.getFileUrl());
        dto.setFileType(material.getFileType());
        dto.setCreatedAt(material.getCreatedAt());
        return dto;
    }
}
