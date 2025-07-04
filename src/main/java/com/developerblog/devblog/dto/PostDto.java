package com.developerblog.devblog.dto;

import com.developerblog.devblog.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private List<Long> tagIds;
    private String authorUsername;
    private List<String> tags;
    private Integer likesCount;
    private Integer dislikesCount;
    private String currentUserVote;
    private List<CommentDto> comments;
    private LocalDateTime createdAt;

    public Integer getTotalVotes() {
        return (likesCount != null ? likesCount : 0) + (dislikesCount != null ? dislikesCount : 0);
    }

    public Integer getLikePercentage() {
        if (getTotalVotes() == 0) return 0;
        return (int) Math.round((likesCount * 100.0) / getTotalVotes());
    }
}