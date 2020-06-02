package com.hitachi.schedule.service.impl;

import com.hitachi.schedule.config.common.DateUtil;
import com.hitachi.schedule.config.common.GCConstGlobals;
import com.hitachi.schedule.controller.param.ArticleDetialInfo;
import com.hitachi.schedule.controller.param.CommentDetialInfo;
import com.hitachi.schedule.controller.param.TitleInfo;
import com.hitachi.schedule.dao.jpa.dao.*;
import com.hitachi.schedule.dao.jpa.entity.*;
import com.hitachi.schedule.dao.mybatis.mapper.annotations.ArticleUserDao;
import com.hitachi.schedule.dao.mybatis.param.ArticeleUserInfo;
import com.hitachi.schedule.service.GSABSScheduleF;
import com.hitachi.schedule.service.param.TitleFindResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

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
    @Autowired
    private LikesDao likesDao;
    @Autowired
    private FolderDao folderDao;
    @Autowired
    private CollectDao collectDao;
    @Autowired
    private ArticleUserDao articleUserDao;


    @Override
    public List<ArticleDetialInfo> getArticleList(String userId) {
        List<Article> db_rtn = articleDao.findTop10ByOrderByArticleUpdateDateDesc();
        return doEditArticleInfoList(db_rtn, userId);
    }

    @Override
    public Map<String, Object> getAllArticleContentById(long articleId) {
        Article article = articleDao.findByArticleId(articleId);
        Map<String, Object> result = new HashMap<>();

        result.put("content", article.getArticleContent());

        Optional<ArticeleUserInfo> infoOptional = articleUserDao.findArticleUserInfo(article.getArticleCreateId());
        if (infoOptional.isPresent()) {
            ArticeleUserInfo articeleUserInfo = infoOptional.get();
            result.put("userInfo", articeleUserInfo);
        }

        return result;
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
            article.setArticleUpdateId(userId);
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
            agree.setUserId(userId);
            agree.setUpdateDate(uDate);
            agreeDao.save(agree);
        }


        return articleAgree;
    }

    @Override
    public Map<String, Object> getCommentList(long articleId, Integer pageNow, Integer sortFlg) {
        Map<String, Object> result = new HashMap<>();

        long commentCount = commentDao.countByArticleId(articleId);
        result.put("commentCount", commentCount);

        if (0 == commentCount) {
            return result;
        }

        commentCount = commentDao.countByArticleIdAndParentIdIsNull(articleId);

        int pageCnt = (int) Math.ceil((double) commentCount / GCConstGlobals.GSAA_PROP_GSABT020_DISPLAY_SIZE);
        if (1 < pageCnt) {
            result.put("pageCnt", pageCnt);
        }
        if (null == pageNow) {
            pageNow = 1;
        }

        if (pageCnt > 1) {
            result.put("pageCnt", pageCnt);
            result.put("pageNow", pageNow);
        }

        PageRequest pageParam = PageRequest.of(pageNow - 1, GCConstGlobals.GSAA_PROP_GSABT020_DISPLAY_SIZE);
        LocalDateTime dateTimeNow = LocalDateTime.now();

        Page<Comment> levelList = null;
        if (null == sortFlg) {
            result.put("levelFlg", true);
            levelList = commentDao.findByArticleIdAndParentIdIsNullOrderByLevelDesc(articleId, pageParam);
        } else {
            levelList = commentDao.findByArticleIdAndParentIdIsNullOrderByUpdateDateDesc(articleId, pageParam);
        }

        result.put("levelList", getCommentDetialList(levelList.getContent(), dateTimeNow, articleId));

        return result;
    }

    @Override
    public long getAndUpdateLikes(long articleId, String userId) {
        Likes likes = likesDao.findByArticleIdAndUpdateId(articleId, userId);
        if (null == likes) {
            likes = new Likes();
            likes.setArticleId(articleId);
            likes.setUpdateId(userId);
            Date uDate = new Date();
            likes.setUpdateDate(uDate);
            likesDao.save(likes);
            return 1;
        } else {
            likesDao.delete(likes);
            return 0;
        }
    }

    @Override
    public Map<String, Object> getCollects(long articleId, String userId) {
        Map<String, Object> result = new HashMap<>();

        List<Folder> collectFolders = folderDao.findByUpdateIdOrderByUpdateDateDesc(userId);
        List<Map<String, Object>> folders = new ArrayList<>();
        for (Folder folder : collectFolders) {

            Map<String, Object> obj = new HashMap<>();
            obj.put("title", folder.getTitle());
            long folderId = folder.getId();
            obj.put("folderId", folderId);
            obj.put("cnt", collectDao.countByFolderId(folderId));
            folders.add(obj);
        }
        result.put("folders", folders);

        Collect collect = collectDao.findByArticleIdAndUpdateId(articleId, userId);
        if (null != collect) {
            result.put("activeId", collect.getFolderId());
        }
        return result;
    }

    @Override
    public void insertFolder(Folder folder) {
        folder.setUpdateDate(new Date());
        folderDao.save(folder);
    }

    @Override
    public void updateCollects(long articleId, String userId, long folderId) {
        Collect collect = collectDao.findByArticleIdAndUpdateId(articleId, userId);
        if (null == collect) {
            collect = new Collect();
            collect.setFolderId(folderId);
            collect.setArticleId(articleId);
            collect.setUpdateId(userId);
            collect.setUpdateDate(new Date());
            collectDao.save(collect);
        } else if (folderId == collect.getFolderId()) {
            collectDao.delete(collect);
        } else {
            collect.setFolderId(folderId);
            collect.setUpdateDate(new Date());
            collectDao.save(collect);
        }
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

    private List<ArticleDetialInfo> getArticleListByTitleId(String userId, long titleId, long articleId) {
        Article article = articleDao.findByArticleId(articleId);
        List<Article> db_rtn = articleDao.findTop10ByArticleTitleIdAndArticleIdNotOrderByArticleAgreeDesc(titleId, articleId);
        db_rtn.add(0, article);
        return doEditArticleInfoList(db_rtn, userId);
    }

    private List<ArticleDetialInfo> doEditArticleInfoList(List<Article> db_rtn, String userId) {
        List<ArticleDetialInfo> articleList = new ArrayList<>();
        for (Article obj : db_rtn) {
            ArticleDetialInfo info = new ArticleDetialInfo();
            long articleId = obj.getArticleId();
            info.setArticleId(articleId);
            long titleId = obj.getArticleTitleId();
            info.setTitleId(titleId);
            Title title = titleDao.findByTitleId(titleId);
            info.setArticleTitle(title.getTitleName());
            info.setArticleAgree(obj.getArticleAgree());
            Agree agree = agreeDao.findByUserIdAndArticleId(userId, articleId);
            info.setArticleAgreeFlg(null == agree ? 0 : agree.isAgree() ? 1 : 2);

            Optional<ArticeleUserInfo> infoOptional = articleUserDao.findArticleUserInfo(obj.getArticleCreateId());
            if (infoOptional.isPresent()) {
                ArticeleUserInfo articeleUserInfo = infoOptional.get();
                info.setUserName(articeleUserInfo.getShkin_smi());
                info.setUserImg(articeleUserInfo.getUser_image());
            }

            info.setArticleLikeFlg(0 != likesDao.countByArticleIdAndUpdateId(articleId, userId));

            String content = obj.getArticleContent();
            info.setArticleContent(content);
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

    private List<CommentDetialInfo> getCommentDetialList(List<Comment> levelList, LocalDateTime dateNow, long articleId) {
        List<CommentDetialInfo> commentDetialInfoList = new ArrayList<>();
        for (Comment comment : levelList) {
            CommentDetialInfo info = new CommentDetialInfo();
            info.setId(comment.getId());
            String userId = comment.getUpdateId();
            info.setUserId(userId);

            Optional<ArticeleUserInfo> infoOptional = articleUserDao.findArticleUserInfo(comment.getUpdateId());
            if (infoOptional.isPresent()) {
                ArticeleUserInfo articeleUserInfo = infoOptional.get();
                info.setUserName(articeleUserInfo.getShkin_smi());
                info.setUserImg(articeleUserInfo.getUser_image());
            }

            info.setLevel(comment.getLevel());
            Date uDate = comment.getUpdateDate();
            info.setDateLong(DateUtil.getDayBetween(uDate, dateNow));
            info.setComment(comment.getComment());

            long parentId = comment.getId();
            List<Comment> childList = commentDao.findTop10ByParentIdOrderByUpdateDateDesc(parentId);
            List<CommentDetialInfo> childs = getCommentDetialList(childList, dateNow, articleId);
            info.setChilds(childs);

            commentDetialInfoList.add(info);
        }
        return commentDetialInfoList;
    }
}
