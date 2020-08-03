package com.hitachi.schedule.controller.handler.gsab;

import com.hitachi.schedule.config.common.SessionUtil;
import com.hitachi.schedule.controller.param.CommentDetialInfo;
import com.hitachi.schedule.dao.jpa.entity.Comment;
import com.hitachi.schedule.service.GSABScheduleF;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Controller
@Slf4j
public class GSABSCommentHandler {
    @Autowired
    private GSABScheduleF gsabService;

    @GetMapping("/GSABSComments/{articleId}")
    @ResponseBody
    public Map<String, Object> GSABSComments(
            @PathVariable("articleId") long articleId,
            Integer pageNow,
            Integer sortParam) {
        return gsabService.getCommentList(articleId, pageNow, sortParam);
    }

    @GetMapping("/GSABSComment/{articleId}")
    @ResponseBody
    public CommentDetialInfo GSABSCommentAdd(
            HttpServletRequest request,
            @PathVariable("articleId") long articleId,
            Long parentId,
            String content) {

        String loginUserId = SessionUtil.getUserId(request);
        Comment comment = new Comment();
        comment.setArticleId(articleId);
        comment.setParentId(parentId);
        comment.setComment(content);
        comment.setUpdateId(loginUserId);
        comment.setUpdateDate(new Date());
        comment.setAgree(0);
        comment.setLevel(0);

        return gsabService.insertComment(comment);
    }
}