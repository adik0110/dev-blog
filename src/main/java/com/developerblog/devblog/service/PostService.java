package com.developerblog.devblog.service;

import com.developerblog.devblog.dto.PostDto;
import com.developerblog.devblog.entity.Post;
import com.developerblog.devblog.entity.Tag;
import com.developerblog.devblog.entity.User;
import com.developerblog.devblog.repository.PostRepository;
import com.developerblog.devblog.repository.TagRepository;
import com.developerblog.devblog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    @Transactional
    public void createPost(PostDto postDto, String username) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setAuthor(author);

        if (postDto.getTagIds() != null && !postDto.getTagIds().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(postDto.getTagIds());
            post.setTags(tags);
        }

        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public List<PostDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
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
                .rating(post.getRating())
                .commentCount(post.getComments() != null ? post.getComments().size() : 0)
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

//    private PostDto convertToDto(Post post) {
//        return PostDto.builder()
//                .id(post.getId())
//                .title(post.getTitle())
//                .content(post.getContent())
//                .authorUsername(post.getAuthor().getUsername())
//                .tags(post.getTags().stream().map(Tag::getName).collect(Collectors.toList()))
//                .rating(post.getRating())
//                .commentCount(post.getComments().size())
//                .createdAt(post.getCreatedAt())
//                .build();
//    }
}