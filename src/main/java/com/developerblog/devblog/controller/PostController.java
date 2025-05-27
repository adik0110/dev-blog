package com.developerblog.devblog.controller;

import com.developerblog.devblog.dto.PostDto;
import com.developerblog.devblog.service.PostService;
import com.developerblog.devblog.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

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
    public String createPost(@ModelAttribute PostDto postDto,
                             Principal principal,
                             Model model) {
        try {
            postService.createPost(postDto, principal.getName());
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("title", "Создание статьи");
            model.addAttribute("content", "posts/create");
            model.addAttribute("error", "Ошибка при создании статьи: " + e.getMessage());
            model.addAttribute("allTags", tagService.getAllTags());
            return "base";
        }
    }

    @GetMapping()
    public String listPosts(Model model) {
        model.addAttribute("title", "Все статьи");
        model.addAttribute("content", "posts/list");
        model.addAttribute("posts", postService.getAllPosts());
        return "base";
    }
}
