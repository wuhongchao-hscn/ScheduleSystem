package com.hitachi.schedule.controller.handler.gsab;

import com.hitachi.schedule.service.GSABSScheduleF;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@Slf4j
public class GSABSCommentHandler {
    @Autowired
    private GSABSScheduleF gsabsService;

    @GetMapping("/GSABSComments/{articleId}")
    @ResponseBody
    public Map<String, Object> GSABSComments(
            @PathVariable("articleId") long articleId,
            Integer pageNow,
            Integer sortParam) {
        return gsabsService.getCommentList(articleId, pageNow, sortParam);
    }
}