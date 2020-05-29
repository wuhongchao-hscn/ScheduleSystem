package com.hitachi.schedule.dao.jpa.dao;

import com.hitachi.schedule.dao.jpa.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LikesDao extends JpaRepository<Likes, Long> {

    long countByArticleIdAndUpdateId(long articleId, String user_id);

    Likes findByArticleIdAndUpdateId(long articleId, String userId);
}
