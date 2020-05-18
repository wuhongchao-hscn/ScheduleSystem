package com.hitachi.schedule.service;

import com.hitachi.schedule.controller.param.ArticleListInfo;
import com.hitachi.schedule.service.param.TitleFindResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GSABSScheduleF {
    List<ArticleListInfo> getArticleList(String userId);

    String getAllArticleContentById(long articleId);

    String getAllTileContentById(long titleId);

    TitleFindResult findByTitle(String userId, long titleId, long articleId);

    @Transactional
    long updateArticleAgree(String userId, long articleId, int agreeFlg);
}
