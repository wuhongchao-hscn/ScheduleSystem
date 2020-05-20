package com.hitachi.schedule.jpa.dao;

import com.hitachi.schedule.jpa.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentDao extends JpaRepository<Comment, Long> {

    long countByArticleId(long articleId);

    long countByArticleIdAndParentIdIsNull(long articleId);

//    List<Comment> findByArticleIdAndLevel(long articleId, long level, Pageable pageParam);

    Page<Comment> findByArticleIdAndParentIdIsNullOrderByLevelDesc(long articleId, Pageable pageParam);

    Page<Comment> findByArticleIdAndParentIdIsNullOrderByUpdateDateDesc(long articleId, Pageable pageParam);

    List<Comment> findTop10ByParentIdOrderByUpdateDateDesc(long parentId);
}
