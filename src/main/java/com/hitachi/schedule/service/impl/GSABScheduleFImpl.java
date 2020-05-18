package com.hitachi.schedule.service.impl;

import com.hitachi.schedule.controller.param.ArticleListInfo;
import com.hitachi.schedule.controller.param.TitleInfo;
import com.hitachi.schedule.jpa.dao.AgreeDao;
import com.hitachi.schedule.jpa.dao.ArticleDao;
import com.hitachi.schedule.jpa.dao.CommentDao;
import com.hitachi.schedule.jpa.dao.TitleDao;
import com.hitachi.schedule.jpa.entity.Agree;
import com.hitachi.schedule.jpa.entity.Article;
import com.hitachi.schedule.jpa.entity.Title;
import com.hitachi.schedule.service.GSABSScheduleF;
import com.hitachi.schedule.service.param.TitleFindResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    @Autowired
    private AgreeDao agreeDao;

    @Override
    public List<ArticleListInfo> getArticleList(String userId) {
        List<Article> db_rtn = articleDao.findTop10ByOrderByArticleUpdateDateDesc();
        return doEditArticleListInfo(db_rtn, userId);
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
    public TitleFindResult findByTitle(String userId, long titleId, long articleId) {
        TitleFindResult tfr = new TitleFindResult();
        tfr.setTitleInfo(getTitleByTitleId(titleId));
        tfr.setArticleAllSize(getArticleAllSize(titleId));
        tfr.setArticleList(getArticleListByTitleId(userId, titleId, articleId));
        return tfr;
    }

    @Override
    public long updateArticleAgree(String userId, long articleId, int agreeParam) {
        Article article = articleDao.findByArticleId(articleId);
        long articleAgree = article.getArticleAgree();
        Date uDate = new Date();

        // agreeParam  stat       articleAgree  agreeTbl
        // 1           ●〇⇒〇〇   -1            D
        // 2           ●〇⇒〇●   -1            U
        // 3           〇●⇒●〇   +1            U
        // 4           〇●⇒〇〇   ×             D
        // 5           〇〇⇒●〇   +1            I
        // 6           〇〇⇒〇●   ×             I
        Agree agree = null;
        if (agreeParam > 4) {
            agree = new Agree();
        } else {
            agree = agreeDao.findByUserIdAndArticleId(userId, articleId);
        }

        if (Arrays.asList(1, 2, 3, 5).contains(agreeParam)) {
            article.setArticleAgree(2 >= agreeParam ? --articleAgree : ++articleAgree);
            article.setArticleUpdateUid(userId);
            article.setArticleUpdateDate(uDate);
            articleDao.save(article);
        }

        if (1 == agreeParam || 4 == agreeParam) {
            // agree cancel または disAgree cancel
            agreeDao.removeByUserIdAndArticleId(userId, articleId);
        } else {
            // agree または disAgree
            agree.setUserId(userId);
            agree.setArticleId(articleId);
            agree.setAgree(Arrays.asList(3, 5).contains(agreeParam));
            agree.setUId(userId);
            agree.setUDate(uDate);
            agreeDao.save(agree);
        }


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

    private List<ArticleListInfo> getArticleListByTitleId(String userId, long titleId, long articleId) {
        Article article = articleDao.findByArticleId(articleId);
        List<Article> db_rtn = articleDao.findTop10ByArticleTitleIdAndArticleIdNotOrderByArticleAgreeDesc(titleId, articleId);
        db_rtn.add(0, article);
        return doEditArticleListInfo(db_rtn, userId);
    }

    private List<ArticleListInfo> doEditArticleListInfo(List<Article> db_rtn, String userId) {
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
            Agree agree = agreeDao.findByUserIdAndArticleId(userId, articleId);
            info.setArticleAgreeFlg(null == agree ? null : agree.isAgree());

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
