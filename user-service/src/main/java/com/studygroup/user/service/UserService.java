package com.studygroup.user.service;

import com.studygroup.user.dto.CreatorApprovalDto;
import com.studygroup.user.dto.UserProfileDto;
import com.studygroup.user.model.UserProfile;
import com.studygroup.user.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserProfileRepository userProfileRepository;

    public UserService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public UserProfileDto getUserProfile(UUID userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User profile not found"));
        return convertToDto(profile);
    }

    public UserProfileDto updateUserProfile(UUID userId, UserProfileDto profileDto) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User profile not found"));

        profile.setFullName(profileDto.getFullName());
        profile.setBio(profileDto.getBio());
        profile.setAvatarUrl(profileDto.getAvatarUrl());
        profile.setUpdatedAt(LocalDateTime.now());

        profile = userProfileRepository.save(profile);
        return convertToDto(profile);
    }

    public List<UserProfileDto> getAllUsers() {
        return userProfileRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void approveCreator(UUID userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User profile not found"));
        
        profile.setCreatorStatus("APPROVED");
        profile.setUpdatedAt(LocalDateTime.now());
        userProfileRepository.save(profile);
    }

    public void rejectCreator(UUID userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User profile not found"));
        
        profile.setCreatorStatus("REJECTED");
        profile.setUpdatedAt(LocalDateTime.now());
        userProfileRepository.save(profile);
    }

    public UserProfile createProfile(UUID userId, String email) {
        UserProfile profile = new UserProfile();
        profile.setId(UUID.randomUUID());
        profile.setUserId(userId);
        profile.setCreatorStatus("NONE");
        profile.setUpdatedAt(LocalDateTime.now());
        
        return userProfileRepository.save(profile);
    }

    private UserProfileDto convertToDto(UserProfile profile) {
        UserProfileDto dto = new UserProfileDto();
        dto.setId(profile.getId());
        dto.setUserId(profile.getUserId());
        dto.setFullName(profile.getFullName());
        dto.setBio(profile.getBio());
        dto.setAvatarUrl(profile.getAvatarUrl());
        dto.setCreatorStatus(profile.getCreatorStatus());
        dto.setUpdatedAt(profile.getUpdatedAt());
        return dto;
    }
}
