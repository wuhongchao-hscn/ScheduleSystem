package com.hitachi.schedule.dao.jpa.dao;

import com.hitachi.schedule.dao.jpa.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ArticleDao extends JpaRepository<Article, Long> {

    Article findByArticleId(long articleId);

    List<Article> findTop10ByOrderByArticleUpdateDateDesc();

    List<Article> findTop10ByArticleTitleIdAndArticleIdNotOrderByArticleAgreeDesc(long articleTitleId, long articleId);

    long countByArticleTitleId(long articleTitleId);
}
