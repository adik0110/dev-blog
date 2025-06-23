package com.developerblog.devblog.repository;

import com.developerblog.devblog.entity.Comment;
import com.developerblog.devblog.entity.Post;
import com.developerblog.devblog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
}
