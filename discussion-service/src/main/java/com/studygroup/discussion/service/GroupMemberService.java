package com.studygroup.discussion.service;

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

    public void addMemberToCache(UUID groupId, UUID userId) {
        String key = groupId.toString() + ":" + userId.toString();
        membershipCache.put(key, true);
        logger.debug("Added membership to cache: {}", key);
    }

    public boolean isMemberCached(UUID groupId, UUID userId) {
        String key = groupId.toString() + ":" + userId.toString();
        return membershipCache.getOrDefault(key, false);
    }

    public void removeMemberFromCache(UUID groupId, UUID userId) {
        String key = groupId.toString() + ":" + userId.toString();
        membershipCache.remove(key);
        logger.debug("Removed membership from cache: {}", key);
    }
}
