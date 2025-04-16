package com.developerblog.devblog.repository;

import com.developerblog.devblog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p ORDER BY p.rating DESC")
    List<Post> findTopRatedPosts();

    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t.name = :tagName")
    List<Post> findByTagName(String tagName);
}
