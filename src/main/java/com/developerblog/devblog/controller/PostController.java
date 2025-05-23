package com.developerblog.devblog.controller;

import com.developerblog.devblog.dto.PostDto;
import com.developerblog.devblog.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

// тут хотел выводить список всех постов на страницу какую нибудь
//    @GetMapping
//    public String listPosts(Model model) {
//        List<PostDto> posts = postService.getAllPosts();
//        model.addAttribute("title", "Статьи");
//        model.addAttribute("posts", posts);
//    }
}
