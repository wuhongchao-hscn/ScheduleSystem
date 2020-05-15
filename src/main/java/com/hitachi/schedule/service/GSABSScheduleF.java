package com.hitachi.schedule.service;

import com.hitachi.schedule.controller.param.ArticleListInfo;
import com.hitachi.schedule.service.param.TitleFindResult;

import java.util.List;

public interface GSABSScheduleF {
    List<ArticleListInfo> getArticleList();

    String getAllArticleContentById(long articleId);

    String getAllTileContentById(long titleId);

    TitleFindResult findByTitle(long titleId, long articleId);

    long updateArticleAgree(long articleId, boolean agreeFlg);
}
