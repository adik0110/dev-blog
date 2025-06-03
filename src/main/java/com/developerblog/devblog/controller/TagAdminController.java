package com.developerblog.devblog.controller;

import com.developerblog.devblog.dto.TagDto;
import com.developerblog.devblog.service.TagService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/tags")
@PreAuthorize("hasRole('ADMIN')")
public class TagAdminController {

    private final TagService tagService;

    public TagAdminController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public String showTags(Model model) {
        model.addAttribute("tags", tagService.getAllTags());
        model.addAttribute("newTag", new TagDto());
        model.addAttribute("content", "admin/tags");
        return "base";
    }

    @PostMapping
    public String createTag(@ModelAttribute("newTag") TagDto tagDto) {
        tagService.createTag(tagDto);
        return "redirect:/admin/tags";
    }

    @PostMapping("/{id}/delete")
    public String deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return "redirect:/admin/tags";
    }
}
