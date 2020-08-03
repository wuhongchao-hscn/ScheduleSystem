package com.hitachi.schedule.controller.handler.gsab;

import com.hitachi.schedule.config.common.SessionUtil;
import com.hitachi.schedule.controller.actionform.GSABS020Form;
import com.hitachi.schedule.dao.jpa.entity.Article;
import com.hitachi.schedule.service.GSABScheduleF;
import com.hitachi.schedule.service.param.TitleFindResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Controller
@Slf4j
public class GSABSTitleHandler {
    @Autowired
    private GSABScheduleF gsabService;

    @GetMapping("/GSABSTitles/{titleId}/{articleId}")
    public String GSABSTitles(
            HttpServletRequest request,
            Model model,
            @PathVariable("titleId") long titleId,
            @PathVariable("articleId") long articleId) {
        GSABS020Form outForm = new GSABS020Form();
        String loginUserId = SessionUtil.getUserId(request);

        TitleFindResult tfr = gsabService.findByTitle(loginUserId, titleId, articleId);

        BeanUtils.copyProperties(tfr, outForm);

        model.addAttribute("form", outForm);

        return "GSABS020";
    }

    @GetMapping("/GSABSTitle/{titleId}")
    @ResponseBody
    public String GSABSTitleGet(@PathVariable("titleId") long titleId) {
        return gsabService.getAllTileContentById(titleId);
    }

    @PostMapping("/GSABSTitle/{titleId}")
    @ResponseBody
    public Map<String, Object> GSABSTitleAdd(
            HttpServletRequest request,
            @PathVariable("titleId") long titleId,
            @RequestParam("inputHtml") String inputHtml) {
        String loginUserId = SessionUtil.getUserId(request);
        Date uDate = new Date();

        Article article = new Article();
        article.setArticleTitleId(titleId);
        article.setArticleContent(inputHtml);
        article.setArticleCreateDate(uDate);
        article.setArticleCreateId(loginUserId);
        article.setArticleUpdateDate(uDate);
        article.setArticleUpdateId(loginUserId);
        article.setArticleAgree(0);
        long articleId = gsabService.insertArticle(article);

        Map<String, Object> result = new HashMap<>();
        result.put("success", articleId);
        return result;
    }
}
