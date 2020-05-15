package com.hitachi.schedule.jpa.dao;

import com.hitachi.schedule.jpa.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentDao extends JpaRepository<Comment, Long> {

    long countByArticleId(long articleId);

}
