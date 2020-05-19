package com.hitachi.schedule.jpa.dao;

import com.hitachi.schedule.jpa.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentDao extends JpaRepository<Comment, Long> {

    long countByArticleId(long articleId);

    List<Comment> findByArticleIdAndLevel(long articleId, long level);
}
