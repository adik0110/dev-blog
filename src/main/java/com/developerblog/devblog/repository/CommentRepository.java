package com.developerblog.devblog.repository;

import com.developerblog.devblog.entity.Comment;
import com.developerblog.devblog.entity.Post;
import com.developerblog.devblog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 1. Найти все комментарии к посту
    List<Comment> findByPost(Post post);

    // 2. Найти все комментарии пользователя
    List<Comment> findByAuthor(User author);

    // 3. Кастомный запрос: топ-5 последних комментариев
    @Query("SELECT c FROM Comment c ORDER BY c.id DESC LIMIT 5")
    List<Comment> findTop5RecentComments();

    // 4. Найти комментарии по содержанию (поиск)
    @Query("SELECT c FROM Comment c WHERE c.text LIKE %:keyword%")
    List<Comment> searchByText(@Param("keyword") String keyword);
}
