package com.studygroup.discussion.service;

import com.studygroup.discussion.model.GroupCreator;
import com.studygroup.discussion.model.GroupMember;
import com.studygroup.discussion.repository.GroupCreatorRepository;
import com.studygroup.discussion.repository.GroupMemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class GroupMemberService {

    private static final Logger logger = LoggerFactory.getLogger(GroupMemberService.class);
    
    // In-memory cache for group memberships
    // In a real production environment, this would be replaced with Redis or another distributed cache
    private final ConcurrentMap<String, Boolean> membershipCache = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Boolean> creatorCache = new ConcurrentHashMap<>();
    private final GroupMemberRepository groupMemberRepository;
    private final GroupCreatorRepository groupCreatorRepository;

    public GroupMemberService(GroupMemberRepository groupMemberRepository,
                              GroupCreatorRepository groupCreatorRepository) {
        this.groupMemberRepository = groupMemberRepository;
        this.groupCreatorRepository = groupCreatorRepository;
    }

    public void addMemberToCache(UUID groupId, UUID userId) {
        String key = cacheKey(groupId, userId);
        membershipCache.put(key, true);
        if (!groupMemberRepository.existsByGroupIdAndUserId(groupId, userId)) {
            groupMemberRepository.save(new GroupMember(groupId, userId));
        }
        logger.debug("Added membership to cache: {}", key);
    }

    public void addCreatorToCache(UUID groupId, UUID creatorId) {
        String key = cacheKey(groupId, creatorId);
        creatorCache.put(key, true);
        groupCreatorRepository.save(new GroupCreator(groupId, creatorId));
        addMemberToCache(groupId, creatorId);
        logger.debug("Added creator to cache: {}", key);
    }

    public boolean isMemberCached(UUID groupId, UUID userId) {
        String key = cacheKey(groupId, userId);
        if (membershipCache.getOrDefault(key, false)) {
            return true;
        }

        boolean exists = groupMemberRepository.existsByGroupIdAndUserId(groupId, userId);
        if (exists) {
            membershipCache.put(key, true);
        }
        return exists;
    }

    public boolean isCreator(UUID groupId, UUID userId) {
        String key = cacheKey(groupId, userId);
        if (creatorCache.getOrDefault(key, false)) {
            return true;
        }

        boolean exists = groupCreatorRepository.existsByGroupIdAndCreatorId(groupId, userId);
        if (exists) {
            creatorCache.put(key, true);
        }
        return exists;
    }

    public void removeMemberFromCache(UUID groupId, UUID userId) {
        String key = cacheKey(groupId, userId);
        membershipCache.remove(key);
        logger.debug("Removed membership from cache: {}", key);
    }

    private String cacheKey(UUID groupId, UUID userId) {
        return groupId.toString() + ":" + userId.toString();
    }
}
