package com.studygroup.discussion.service;

import com.studygroup.discussion.dto.PostDto;
import com.studygroup.discussion.model.Post;
import com.studygroup.discussion.model.Reply;
import com.studygroup.discussion.repository.PostRepository;
import com.studygroup.discussion.repository.ReplyRepository;
import com.studygroup.discussion.repository.GroupMemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;
    private final GroupMemberRepository groupMemberRepository;

    public PostService(PostRepository postRepository, ReplyRepository replyRepository,
                       GroupMemberRepository groupMemberRepository) {
        this.postRepository = postRepository;
        this.replyRepository = replyRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    public Page<PostDto> getPosts(UUID groupId, Pageable pageable) {
        return postRepository.findByGroupIdOrderByCreatedAtDesc(groupId, pageable)
                .map(this::convertToDto);
    }

    public PostDto getPost(UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return convertToDto(post);
    }

    public PostDto createPost(UUID groupId, PostDto postDto, UUID authorId) {
        Post post = new Post();
        post.setId(UUID.randomUUID());
        post.setGroupId(groupId);
        post.setAuthorId(authorId);
        post.setContent(postDto.getContent());
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        post = postRepository.save(post);
        return convertToDto(post);
    }

    public PostDto updatePost(UUID postId, PostDto postDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setContent(postDto.getContent());
        post.setUpdatedAt(LocalDateTime.now());

        post = postRepository.save(post);
        return convertToDto(post);
    }

    public void deletePost(UUID postId) {
        postRepository.deleteById(postId);
    }

    public void addReply(UUID postId, String content, UUID authorId) {
        Reply reply = new Reply();
        reply.setId(UUID.randomUUID());
        reply.setPostId(postId);
        reply.setAuthorId(authorId);
        reply.setContent(content);
        reply.setCreatedAt(LocalDateTime.now());

        replyRepository.save(reply);
    }

    public boolean isGroupMember(UUID groupId, UUID userId) {
        return groupMemberRepository.existsByGroupIdAndUserId(groupId, userId);
    }

    public boolean isGroupCreator(UUID groupId, UUID userId) {
        // This would typically call the group service to verify
        // For now, we'll assume group creators are also members
        return isGroupMember(groupId, userId);
    }

    private PostDto convertToDto(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setGroupId(post.getGroupId());
        dto.setAuthorId(post.getAuthorId());
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        
        // Load replies
        List<Reply> replies = replyRepository.findByPostIdOrderByCreatedAtAsc(post.getId());
        dto.setReplies(replies.stream()
                .map(this::convertReplyToDto)
                .collect(Collectors.toList()));
        
        return dto;
    }

    private PostDto.ReplyDto convertReplyToDto(Reply reply) {
        PostDto.ReplyDto dto = new PostDto.ReplyDto();
        dto.setId(reply.getId());
        dto.setPostId(reply.getPostId());
        dto.setAuthorId(reply.getAuthorId());
        dto.setContent(reply.getContent());
        dto.setCreatedAt(reply.getCreatedAt());
        return dto;
    }
}
