package com.hitachi.schedule.controller.handler.gsab;

import com.hitachi.schedule.config.common.SessionUtil;
import com.hitachi.schedule.config.component.CommonUtil;
import com.hitachi.schedule.config.component.MessageReadUtil;
import com.hitachi.schedule.controller.actionform.GSABS010Form;
import com.hitachi.schedule.service.GSABSScheduleF;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;


@Controller
@Slf4j
public class GSABDisplayHandler {
    @Autowired
    private MessageReadUtil messageUtil;
    @Autowired
    private GSABSScheduleF gsabsService;
    @Autowired
    private CommonUtil commonUtil;

    @GetMapping("/GSABS010Display")
    public String GSABS010Display(
            HttpServletRequest request,
            Model model) {
        GSABS010Form outForm = new GSABS010Form();

        String loginUserId = SessionUtil.getUserId(request);
        outForm.setArticleList(gsabsService.getArticleList(loginUserId));

        model.addAttribute("form", outForm);

        log.info("ブログメイン画面を表示します。");
        return "GSABS010";
    }
}
