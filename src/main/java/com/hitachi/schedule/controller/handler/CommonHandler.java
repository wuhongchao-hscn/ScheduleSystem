package com.hitachi.schedule.controller.handler;

import com.hitachi.schedule.controller.common.SessionUtil;
import com.hitachi.schedule.controller.component.MessageReadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;


@Controller
@Slf4j
public class CommonHandler {
    @Autowired
    private MessageReadUtil messageUtil;

    @RequestMapping("/commonLogoutSucess")
    public String commonLogoutSucess(
            HttpServletRequest request,
            RedirectAttributes redirectModel) {
        String loginMsg = messageUtil.getMessage("GSAXM009I");
        redirectModel.addFlashAttribute("warning", loginMsg);

        SessionUtil.clearSessionValue(request);
        return "redirect:/GSAXS010Display";
    }

    @GetMapping("/commonUserDetail")
    public String commonUserDetail(HttpServletRequest request) {
        log.info("ユーザ情報リンクを押下しました。");

        String loginUserId = SessionUtil.getUserId(request);
        return "forward:/GSACS050Detail?userId=" + loginUserId;
    }
}
