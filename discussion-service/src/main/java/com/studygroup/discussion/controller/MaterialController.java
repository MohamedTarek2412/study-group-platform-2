package com.studygroup.discussion.controller;

import com.studygroup.discussion.dto.MaterialDto;
import com.studygroup.discussion.service.MaterialService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/groups/{groupId}/materials")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping
    public ResponseEntity<Page<MaterialDto>> getMaterials(
            @PathVariable UUID groupId,
            @RequestHeader("X-User-Id") String userId,
            Pageable pageable) {
        
        // Check if user is a member of the group
        if (!materialService.isGroupMember(groupId, UUID.fromString(userId))) {
            return ResponseEntity.status(403).build();
        }
        
        return ResponseEntity.ok(materialService.getMaterials(groupId, pageable));
    }

    @PostMapping
    public ResponseEntity<MaterialDto> uploadMaterial(
            @PathVariable UUID groupId,
            @Valid @RequestBody MaterialDto materialDto,
            @RequestHeader("X-User-Id") String userId) {
        
        // Check if user is a member of the group
        if (!materialService.isGroupMember(groupId, UUID.fromString(userId))) {
            return ResponseEntity.status(403).build();
        }
        
        return ResponseEntity.ok(materialService.uploadMaterial(groupId, materialDto, UUID.fromString(userId)));
    }

    @DeleteMapping("/{materialId}")
    public ResponseEntity<Void> deleteMaterial(
            @PathVariable UUID groupId,
            @PathVariable UUID materialId,
            @RequestHeader("X-User-Id") String userId) {
        
        // Check if user is the uploader of the material or group creator
        MaterialDto material = materialService.getMaterial(materialId);
        if (!material.getUploaderId().toString().equals(userId) && 
            !materialService.isGroupCreator(groupId, UUID.fromString(userId))) {
            return ResponseEntity.status(403).build();
        }
        
        materialService.deleteMaterial(materialId);
        return ResponseEntity.ok().build();
    }
}
