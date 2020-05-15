package com.hitachi.schedule.service.impl;

import com.hitachi.schedule.controller.param.ArticleListInfo;
import com.hitachi.schedule.controller.param.TitleInfo;
import com.hitachi.schedule.jpa.dao.ArticleDao;
import com.hitachi.schedule.jpa.dao.CommentDao;
import com.hitachi.schedule.jpa.dao.TitleDao;
import com.hitachi.schedule.jpa.entity.Article;
import com.hitachi.schedule.jpa.entity.Title;
import com.hitachi.schedule.service.GSABSScheduleF;
import com.hitachi.schedule.service.param.TitleFindResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GSABScheduleFImpl implements GSABSScheduleF {
    private static final int contentLen = 200;
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private TitleDao titleDao;

    @Override
    public List<ArticleListInfo> getArticleList() {
        List<Article> db_rtn = articleDao.findTop10ByOrderByArticleUpdateDateDesc();
        return doEditArticleListInfo(db_rtn);
    }

    @Override
    public String getAllArticleContentById(long articleId) {
        Article article = articleDao.findByArticleId(articleId);
        return article.getArticleContent();
    }

    @Override
    public String getAllTileContentById(long titleId) {
        Title title = titleDao.findByTitleId(titleId);
        return title.getTitleContent();
    }

    @Override
    public TitleFindResult findByTitle(long titleId, long articleId) {
        TitleFindResult tfr = new TitleFindResult();
        tfr.setTitleInfo(getTitleByTitleId(titleId));
        tfr.setArticleAllSize(getArticleAllSize(titleId));
        tfr.setArticleList(getArticleListByTitleId(titleId, articleId));
        return tfr;
    }

    @Override
    public long updateArticleAgree(long articleId, int agreeParam) {
        Article article = articleDao.findByArticleId(articleId);
        long articleAgree = article.getArticleAgree();

        if (1 == agreeParam) {
            // agree cancel
            articleAgree -= 1l;
        } else if (2 == agreeParam) {
            // agree
            articleAgree += 1l;
        } else if (3 == agreeParam) {
            // disAgree cancel
        } else {
            // disAgree
        }
        articleDao.save(article);

        return articleAgree;
    }

    private TitleInfo getTitleByTitleId(long titleId) {
        TitleInfo info = new TitleInfo();
        Title title = titleDao.findByTitleId(titleId);
        info.setTitleId(titleId);
        info.setTitleName(title.getTitleName());

        String content = title.getTitleContent();
        if (content.length() > contentLen) {
            content = content.substring(0, contentLen);
            info.setContentFlg(true);
        }
        info.setTitleContent(content);

        return info;
    }

    private long getArticleAllSize(long titleId) {
        return articleDao.countByArticleTitleId(titleId);
    }

    private List<ArticleListInfo> getArticleListByTitleId(long titleId, long articleId) {
        Article article = articleDao.findByArticleId(articleId);
        List<Article> db_rtn = articleDao.findTop10ByArticleTitleIdAndArticleIdNotOrderByArticleAgreeDesc(titleId, articleId);
        db_rtn.add(0, article);
        return doEditArticleListInfo(db_rtn);
    }

    private List<ArticleListInfo> doEditArticleListInfo(List<Article> db_rtn) {
        List<ArticleListInfo> articleList = new ArrayList<>();
        for (Article obj : db_rtn) {
            ArticleListInfo info = new ArticleListInfo();
            long articleId = obj.getArticleId();
            info.setArticleId(articleId);
            long titleId = obj.getArticleTitleId();
            info.setTitleId(titleId);
            Title title = titleDao.findByTitleId(titleId);
            info.setArticleTitle(title.getTitleName());
            info.setArticleAgree(obj.getArticleAgree());

            String content = obj.getArticleContent();
            int startIndex = content.indexOf("<img src=");
            int endIndex = 0;
            if (startIndex >= 0) {
                String prefix = content.substring(0, startIndex);
                String suffix = content.substring(startIndex);

                endIndex = suffix.indexOf(">") + 1;
                String articleImg = suffix.substring(0, endIndex);
                info.setArticleImg(articleImg);

                suffix = content.substring(endIndex);
                content = prefix.concat(suffix);
            }

            if (content.length() > contentLen) {
                content = content.substring(0, contentLen);
                info.setContentFlg(true);
            }

            info.setArticleContent(content);

            long articleCount = commentDao.countByArticleId(articleId);
            info.setArticleCount(articleCount);

            articleList.add(info);
        }
        return articleList;
    }
}
