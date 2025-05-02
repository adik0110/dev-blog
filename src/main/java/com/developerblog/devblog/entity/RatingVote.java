package com.developerblog.devblog.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "rating_votes")
@Data
public class RatingVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private Integer value; // +1 или -1
}