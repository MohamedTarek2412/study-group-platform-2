package com.studygroup.discussion.controller;

import com.studygroup.discussion.dto.PostDto;
import com.studygroup.discussion.service.PostService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/groups/{groupId}/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<Page<PostDto>> getPosts(
            @PathVariable UUID groupId,
            @RequestHeader("X-User-Id") String userId,
            Pageable pageable) {
        
        // Check if user is a member of the group
        if (!postService.isGroupMember(groupId, UUID.fromString(userId))) {
            return ResponseEntity.status(403).build();
        }
        
        return ResponseEntity.ok(postService.getPosts(groupId, pageable));
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @PathVariable UUID groupId,
            @Valid @RequestBody PostDto postDto,
            @RequestHeader("X-User-Id") String userId) {
        
        // Check if user is a member of the group
        if (!postService.isGroupMember(groupId, UUID.fromString(userId))) {
            return ResponseEntity.status(403).build();
        }
        
        return ResponseEntity.ok(postService.createPost(groupId, postDto, UUID.fromString(userId)));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable UUID groupId,
            @PathVariable UUID postId,
            @Valid @RequestBody PostDto postDto,
            @RequestHeader("X-User-Id") String userId) {
        
        // Check if user is the author of the post
        PostDto existingPost = postService.getPost(postId);
        if (!existingPost.getAuthorId().toString().equals(userId)) {
            return ResponseEntity.status(403).build();
        }
        
        return ResponseEntity.ok(postService.updatePost(postId, postDto));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable UUID groupId,
            @PathVariable UUID postId,
            @RequestHeader("X-User-Id") String userId) {
        
        // Check if user is the author of the post or group creator
        PostDto post = postService.getPost(postId);
        if (!post.getAuthorId().toString().equals(userId) && 
            !postService.isGroupCreator(groupId, UUID.fromString(userId))) {
            return ResponseEntity.status(403).build();
        }
        
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{postId}/replies")
    public ResponseEntity<Void> addReply(
            @PathVariable UUID groupId,
            @PathVariable UUID postId,
            @RequestBody String content,
            @RequestHeader("X-User-Id") String userId) {
        
        // Check if user is a member of the group
        if (!postService.isGroupMember(groupId, UUID.fromString(userId))) {
            return ResponseEntity.status(403).build();
        }
        
        postService.addReply(postId, content, UUID.fromString(userId));
        return ResponseEntity.ok().build();
    }
}
