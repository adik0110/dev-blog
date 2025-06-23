package com.developerblog.devblog.service;

import com.developerblog.devblog.dto.PostDto;
import com.developerblog.devblog.entity.*;
import com.developerblog.devblog.repository.PostRepository;
import com.developerblog.devblog.repository.RatingVoteRepository;
import com.developerblog.devblog.repository.TagRepository;
import com.developerblog.devblog.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final CommentService commentService;
    public final RatingVoteRepository ratingVoteRepository;

    @Transactional
    public PostDto createPost(PostDto postDto, String username, List<Long> tagIds) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setAuthor(author);

        if (tagIds != null && !tagIds.isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(tagIds);
            post.setTags(tags);
        }

        postRepository.save(post);
        return postDto;
    }

    @Transactional(readOnly = true)
    public List<PostDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostDto getPostById(Long id, String username) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));

        PostDto postDto = convertToDto(post);

        if (username != null) {
            Optional<RatingVote> userVote = ratingVoteRepository.findByPostAndUserUsername(post, username);
            userVote.ifPresent(vote -> postDto.setCurrentUserVote(vote.getVoteType().name()));
        }

        return postDto;
    }

    @Transactional
    public void processVote(Long postId, String username, String voteTypeStr) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        VoteType voteType = VoteType.valueOf(voteTypeStr);

        Optional<RatingVote> existingVote = ratingVoteRepository.findByPostAndUser(post, user);

        if (existingVote.isPresent()) {
            RatingVote vote = existingVote.get();
            if (vote.getVoteType() == voteType) {
                ratingVoteRepository.delete(vote);
                if (voteType == VoteType.LIKE) {
                    post.setLikesCount(post.getLikesCount() - 1);
                } else {
                    post.setDislikesCount(post.getDislikesCount() - 1);
                }
            } else {
                vote.setVoteType(voteType);
                if (voteType == VoteType.LIKE) {
                    post.setLikesCount(post.getLikesCount() + 1);
                    post.setDislikesCount(post.getDislikesCount() - 1);
                } else {
                    post.setLikesCount(post.getLikesCount() - 1);
                    post.setDislikesCount(post.getDislikesCount() + 1);
                }
                ratingVoteRepository.save(vote);
            }
        } else {
            RatingVote newVote = new RatingVote();
            newVote.setPost(post);
            newVote.setUser(user);
            newVote.setVoteType(voteType);
            ratingVoteRepository.save(newVote);

            if (voteType == VoteType.LIKE) {
                post.setLikesCount(post.getLikesCount() + 1);
            } else {
                post.setDislikesCount(post.getDislikesCount() + 1);
            }
        }

        postRepository.save(post);
    }

    @Transactional
    public PostDto updatePost(Long postId, PostDto postDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());

        if (postDto.getTagIds() != null && !postDto.getTagIds().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(postDto.getTagIds());
            post.setTags(tags);
        }

        postRepository.save(post);
        return postDto;
    }

    @Transactional
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    private PostDto convertToDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorUsername(post.getAuthor().getUsername())
                .tags(post.getTags() != null ?
                        post.getTags().stream().map(Tag::getName).collect(Collectors.toList()) :
                        Collections.emptyList())
                .likesCount(post.getLikesCount())
                .dislikesCount(post.getDislikesCount())
                .comments(commentService.getCommentsByPostId(post.getId()))
                .createdAt(post.getCreatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public List<PostDto> get5TopRecentPosts() {
        List<Post> posts = postRepository.findTop5RecentPosts();
        return posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

}