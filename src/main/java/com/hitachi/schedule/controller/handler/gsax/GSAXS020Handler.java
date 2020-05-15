package com.hitachi.schedule.controller.handler.gsax;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class GSAXS020Handler {

    @GetMapping("/GSAXS020Schedule")
    public String GSAXS020Schedule() {
        log.info("スケジュール管理ボタンを押下しました。");
        return "redirect:GSAAS020Display";
    }

    @GetMapping("/GSAXS020User")
    public String GSAXS020User() {
        log.info("利用者管理ボタンを押下しました。");
        return "redirect:GSACS010Display";
    }

    @GetMapping("/GSAXS020Blog")
    public String GSAXS020Blog() {
        log.info("ブログボタンを押下しました。");
        return "redirect:GSABS010Display";
    }

}
