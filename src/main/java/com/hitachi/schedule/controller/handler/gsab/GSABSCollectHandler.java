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
public class GSABSCollectHandler {
    @Autowired
    private GSABScheduleF gsabService;

    @GetMapping("/GSABSCollectList/{articleId}")
    @ResponseBody
    public Map<String, Object> GSABSCollectList(
            HttpServletRequest request,
            @PathVariable("articleId") long articleId) {
        String loginUserId = SessionUtil.getUserId(request);
        return gsabService.getCollects(articleId, loginUserId);
    }

    @GetMapping("/GSABSCollect/{articleId}/{folderId}")
    @ResponseBody
    public void GSABSCollect(
            HttpServletRequest request,
            @PathVariable("articleId") long articleId,
            @PathVariable("folderId") long folderId) {
        String loginUserId = SessionUtil.getUserId(request);
        gsabService.updateCollects(articleId, loginUserId, folderId);
    }


}
