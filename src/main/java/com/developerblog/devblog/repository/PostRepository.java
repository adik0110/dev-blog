package com.developerblog.devblog.repository;

import com.developerblog.devblog.entity.Comment;
import com.developerblog.devblog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p ORDER BY p.id DESC LIMIT 5")
    List<Post> findTop5RecentPosts();
}
