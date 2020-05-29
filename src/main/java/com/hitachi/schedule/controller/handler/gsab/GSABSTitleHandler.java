package com.hitachi.schedule.controller.handler.gsab;

import com.hitachi.schedule.config.common.SessionUtil;
import com.hitachi.schedule.controller.actionform.GSABS020Form;
import com.hitachi.schedule.service.GSABSScheduleF;
import com.hitachi.schedule.service.param.TitleFindResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


@Controller
@Slf4j
public class GSABSTitleHandler {
    @Autowired
    private GSABSScheduleF gsabsService;

    @GetMapping("/GABSTitles/{titleId}/{articleId}")
    public String GABSTitles(
            HttpServletRequest request,
            Model model,
            @PathVariable("titleId") long titleId,
            @PathVariable("articleId") long articleId) {
        GSABS020Form outForm = new GSABS020Form();
        String loginUserId = SessionUtil.getUserId(request);

        TitleFindResult tfr = gsabsService.findByTitle(loginUserId, titleId, articleId);

        BeanUtils.copyProperties(tfr, outForm);

        model.addAttribute("form", outForm);

        return "GSABS020";
    }

    @GetMapping("/GSABSTitle/{titleId}")
    @ResponseBody
    public String GSABSTitle(@PathVariable("titleId") long titleId) {
        return gsabsService.getAllTileContentById(titleId);
    }


}
