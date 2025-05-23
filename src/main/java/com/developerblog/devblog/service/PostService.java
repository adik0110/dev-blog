package com.developerblog.devblog.service;

import com.developerblog.devblog.dto.PostDto;
import com.developerblog.devblog.entity.Post;
import com.developerblog.devblog.entity.Tag;
import com.developerblog.devblog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    // тут хотел передать в дто все посты
//    @Transactional(readOnly = true)
//    public List<PostDto> getAllPosts() {
//        List<Post> posts = postRepository.findAll();
//        return posts.stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
//    }

    @Transactional(readOnly = true)
    public List<PostDto> get5TopRecentPosts() {
        List<Post> posts = postRepository.findTop5RecentPosts();
        return posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private PostDto convertToDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorUsername(post.getAuthor().getUsername())
                .tags(post.getTags().stream().map(Tag::getName).collect(Collectors.toList()))
                .rating(post.getRating())
                .commentCount(post.getComments().size())
                .createdAt(post.getCreatedAt())
                .build();
    }
}