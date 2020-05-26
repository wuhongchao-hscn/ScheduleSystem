package com.hitachi.schedule.service;

import com.hitachi.schedule.controller.param.ArticleDetialInfo;
import com.hitachi.schedule.jpa.entity.Folder;
import com.hitachi.schedule.service.param.TitleFindResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface GSABSScheduleF {
    List<ArticleDetialInfo> getArticleList(String userId);

    String getAllArticleContentById(long articleId);

    String getAllTileContentById(long titleId);

    TitleFindResult findByTitle(String userId, long titleId, long articleId);

    @Transactional
    long updateArticleAgree(String userId, long articleId, int agreeFlg);

    Map<String, Object> getCommentList(long articleId, Integer pageNow, Integer sortFlg);

    long getAndUpdateLikes(long articleId, String userId);

    Map<String, Object> getCollects(long articleId, String userId);

    Map<String, Object> insertFolder(Folder folder);
}
