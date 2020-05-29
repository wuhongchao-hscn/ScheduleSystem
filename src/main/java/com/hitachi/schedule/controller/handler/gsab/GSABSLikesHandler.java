package com.hitachi.schedule.controller.handler.gsab;

import com.hitachi.schedule.config.common.SessionUtil;
import com.hitachi.schedule.service.GSABSScheduleF;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


@Controller
@Slf4j
public class GSABSLikesHandler {
    @Autowired
    private GSABSScheduleF gsabsService;

    @GetMapping("/GSABSLikes/{articleId}")
    @ResponseBody
    public long GSABSLikes(
            HttpServletRequest request,
            @PathVariable("articleId") long articleId) {
        String loginUserId = SessionUtil.getUserId(request);
        return gsabsService.getAndUpdateLikes(articleId, loginUserId);
    }


}
