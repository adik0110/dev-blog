package com.developerblog.devblog.controller;

import com.developerblog.devblog.dto.PostDto;
import com.developerblog.devblog.service.PostService;
import com.developerblog.devblog.service.TagService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final TagService tagService;

    public PostController(PostService postService, TagService tagService) {
        this.postService = postService;
        this.tagService = tagService;
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, Principal principal) {
        model.addAttribute("title", "Создание статьи");
        model.addAttribute("content", "posts/create");
        model.addAttribute("postDto", new PostDto());
        model.addAttribute("allTags", tagService.getAllTags());
        return "base";
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody @Valid PostDto postDto, Principal principal) {
        try {
            postService.createPost(postDto, principal.getName(), postDto.getTagIds());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @Data
    private static class ErrorResponse {
        private final String message;
    }


    @GetMapping()
    public String listPosts(Model model) {
        model.addAttribute("title", "Все статьи");
        model.addAttribute("content", "posts/list");
        model.addAttribute("posts", postService.getAllPosts());
        return "base";
    }

    @GetMapping("/{postId}")
    public String getPostDetails(@PathVariable Long postId,
                                 Principal principal,
                                 Model model) {
        PostDto postDto = postService.getPostById(postId, principal != null ? principal.getName() : null);
        model.addAttribute("title", postDto.getTitle());
        model.addAttribute("content", "posts/detail");
        model.addAttribute("post", postDto);

        boolean isAuthor = principal != null &&
                principal.getName().equals(postDto.getAuthorUsername());
        model.addAttribute("isAuthor", isAuthor);

        return "base";
    }

    @PostMapping("/{postId}/like")
    public String likePost(@PathVariable Long postId,
                           Principal principal) {
        if (principal == null) {
            throw new AccessDeniedException("Для голосования необходимо авторизоваться");
        }
        postService.processVote(postId, principal.getName(), "LIKE");
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{postId}/dislike")
    public String dislikePost(@PathVariable Long postId,
                              Principal principal) {
        if (principal == null) {
            throw new AccessDeniedException("Для голосования необходимо авторизоваться");
        }
        postService.processVote(postId, principal.getName(), "DISLIKE");
        return "redirect:/posts/" + postId;
    }

    @GetMapping("/{postId}/edit")
    public String showEditForm(@PathVariable Long postId,
                               Principal principal,
                               Model model) {
        PostDto postDto = postService.getPostById(postId, principal != null ? principal.getName() : null);

        if (!principal.getName().equals(postDto.getAuthorUsername())) {
            throw new AccessDeniedException("Вы не можете редактировать этот пост");
        }

        model.addAttribute("title", "Редактирование статьи");
        model.addAttribute("content", "posts/edit");
        model.addAttribute("postDto", postDto);
        model.addAttribute("allTags", tagService.getAllTags());
        return "base";
    }

    @PostMapping("/{postId}/edit")
    public String updatePost(@PathVariable Long postId,
                             @ModelAttribute PostDto postDto,
                             Principal principal) {
        PostDto existingPost = postService.getPostById(postId, principal != null ? principal.getName() : null);
        if (!principal.getName().equals(existingPost.getAuthorUsername())) {
            throw new AccessDeniedException("Вы не можете редактировать этот пост");
        }

        postService.updatePost(postId, postDto);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{postId}/delete")
    public String deletePost(@PathVariable Long postId,
                             Principal principal) {
        PostDto existingPost = postService.getPostById(postId, principal != null ? principal.getName() : null);
        if (!principal.getName().equals(existingPost.getAuthorUsername())) {
            throw new AccessDeniedException("Вы не можете удалить этот пост");
        }

        postService.deletePost(postId);
        return "redirect:/";
    }
}
