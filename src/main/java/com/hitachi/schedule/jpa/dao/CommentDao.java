package com.hitachi.schedule.jpa.dao;

import com.hitachi.schedule.jpa.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentDao extends JpaRepository<Comment, Long> {

    long countByArticleId(long articleId);

//    List<Comment> findByArticleIdAndLevel(long articleId, long level, Pageable pageParam);

    Page<Comment> findByArticleIdOrderByLevelDesc(long articleId, Pageable pageParam);

    Page<Comment> findByArticleIdOrderByUpdateDateDesc(long articleId, Pageable pageParam);
}
