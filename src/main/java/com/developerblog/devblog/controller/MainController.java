package com.developerblog.devblog.controller;

import com.developerblog.devblog.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private final PostService postService;

    public MainController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "Главная страница");
        model.addAttribute("content", "index");
        model.addAttribute("latestPosts", postService.get5TopRecentPosts());
        return "base";
    }
}
