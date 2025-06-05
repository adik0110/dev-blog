package com.developerblog.devblog.api;

import com.developerblog.devblog.dto.PostDto;
import com.developerblog.devblog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostRestController {

    private final PostService postService;

    @GetMapping
    public List<PostDto> listPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public PostDto getPost(@PathVariable Long id, Principal principal) {
        return postService.getPostById(id,
                principal != null ? principal.getName() : null);
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody @Valid PostDto postDto,
                                              Principal principal) {
        if (principal == null) {
            throw new AccessDeniedException("Требуется авторизация для создания статьи");
        }

        PostDto created = postService.createPost(postDto, principal.getName());

        /* Заголовок Location: /api/posts/{newId} */
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    /* ---------------------------------------------------------------------
       PATCH /api/posts/{id} – частичное редактирование
       (можно заменить на PUT, если хотите полную замену)
       --------------------------------------------------------------------- */
    @PatchMapping("/{id}")
    public PostDto updatePost(@PathVariable Long id,
                              @RequestBody @Valid PostDto postDto,
                              Principal principal) {
        if (principal == null) {
            throw new AccessDeniedException("Требуется авторизация");
        }

        PostDto existing = postService.getPostById(id, principal.getName());
        if (!existing.getAuthorUsername().equals(principal.getName())) {
            throw new AccessDeniedException("Вы не автор этой статьи");
        }

        return postService.updatePost(id, postDto);
    }

    /* ---------------------------------------------------------------------
       DELETE /api/posts/{id} – удалить статью
       --------------------------------------------------------------------- */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id,
                                           Principal principal) {
        if (principal == null) {
            throw new AccessDeniedException("Требуется авторизация");
        }

        PostDto existing = postService.getPostById(id, principal.getName());
        if (!existing.getAuthorUsername().equals(principal.getName())) {
            throw new AccessDeniedException("Вы не автор этой статьи");
        }

        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
