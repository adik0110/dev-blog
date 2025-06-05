package com.developerblog.devblog.repository;

import com.developerblog.devblog.entity.Post;
import com.developerblog.devblog.entity.RatingVote;
import com.developerblog.devblog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RatingVoteRepository extends JpaRepository<RatingVote, Long> {
    Optional<RatingVote> findByPostAndUser(Post post, User user);
    Optional<RatingVote> findByPostAndUserUsername(Post post, String username);
}