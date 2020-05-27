package com.hitachi.schedule.controller.handler.gsab;

import com.hitachi.schedule.controller.common.SessionUtil;
import com.hitachi.schedule.service.GSABSScheduleF;
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
    private GSABSScheduleF gsabsService;

    @GetMapping("/GSABSCollectList/{articleId}")
    @ResponseBody
    public Map<String, Object> GSABSCollectList(
            HttpServletRequest request,
            @PathVariable("articleId") long articleId) {
        String loginUserId = SessionUtil.getUserId(request);
        return gsabsService.getCollects(articleId, loginUserId);
    }

    @GetMapping("/GSABSCollect/{articleId}/{folderId}")
    @ResponseBody
    public void GSABSCollect(
            HttpServletRequest request,
            @PathVariable("articleId") long articleId,
            @PathVariable("folderId") long folderId) {
        String loginUserId = SessionUtil.getUserId(request);
        gsabsService.updateCollects(articleId, loginUserId, folderId);
    }


}
