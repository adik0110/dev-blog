package com.developerblog.devblog.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private String authorUsername; // Упрощенное представление автора
    private List<String> tags;     // Только названия тегов
    private Integer rating;
    private Integer commentCount;  // Количество комментариев
    private LocalDateTime createdAt;
}