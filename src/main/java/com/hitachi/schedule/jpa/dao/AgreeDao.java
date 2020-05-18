package com.hitachi.schedule.jpa.dao;

import com.hitachi.schedule.jpa.entity.Agree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface AgreeDao extends JpaRepository<Agree, Long> {

    Agree findByUserIdAndArticleId(String userId, long articleId);

    @Transactional
    void removeByUserIdAndArticleId(String userId, long articleId);
}
