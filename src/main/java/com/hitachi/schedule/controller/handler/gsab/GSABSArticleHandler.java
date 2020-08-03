package com.hitachi.schedule.controller.handler.gsab;

import com.hitachi.schedule.config.common.SessionUtil;
import com.hitachi.schedule.service.GSABScheduleF;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@Controller
@Slf4j
public class GSABSArticleHandler {
    @Autowired
    private GSABScheduleF gsabService;

    @GetMapping("/GSABSArticle/{articleId}")
    @ResponseBody
    public Map<String, Object> GSABSArticle(
            @PathVariable("articleId") long articleId) {
        return gsabService.getAllArticleContentById(articleId);
    }

    @GetMapping("/GSABSArticleAgree/{articleId}")
    @ResponseBody
    public long GSABSArticleAgree(
            HttpServletRequest request,
            @PathVariable("articleId") long articleId,
            int agreeParam) {
        String loginUserId = SessionUtil.getUserId(request);
        return gsabService.updateArticleAgree(loginUserId, articleId, agreeParam);
    }
}
