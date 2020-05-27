package com.hitachi.schedule.jpa.dao;

import com.hitachi.schedule.jpa.entity.Collect;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CollectDao extends JpaRepository<Collect, Long> {
    long countByFolderId(long folderId);

    Collect findByArticleIdAndUpdateId(long articleId, String userId);
}
