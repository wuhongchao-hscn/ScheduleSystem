package com.hitachi.schedule.controller.handler.gsax;

import com.hitachi.schedule.controller.actionform.GSAXS010Form;
import com.hitachi.schedule.controller.actionform.GSAXS020Form;
import com.hitachi.schedule.controller.common.GCConstGlobals;
import com.hitachi.schedule.controller.common.SessionUtil;
import com.hitachi.schedule.service.GSAXScheduleF;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;


@Controller
@Slf4j
public class GSAXDisplayHandler {
    @Autowired
    private GSAXScheduleF gsaxService;

    @GetMapping("/GSAXS010Display")
    public String GSAXS010Display(Model model) {
        GSAXS010Form outForm = new GSAXS010Form();
        model.addAttribute("form", outForm);
        log.info("ログイン画面を表示します。");
        return "GSAXS010";
    }

    @GetMapping(value = {"/", "/GSAXS020Display"})
    public String GSAXS020Display(
            HttpServletRequest request,
            Model model) {
        log.info("メニュー画面を表示します。");

        SessionUtil.removeSessionValue(request,
                Arrays.asList(
                        GCConstGlobals.GSAA_PROP_GSACT010_KNSK_JUKN,
                        GCConstGlobals.GSAA_PROP_GSACT010_KNSK_KEKA,
                        GCConstGlobals.GSAA_PROP_GSACT020_USER_ID,
                        GCConstGlobals.GSAA_PROP_GSACT030_USER_KEKA,
                        GCConstGlobals.GSAA_PROP_GSAAT020_KNSK_JUKN,
                        GCConstGlobals.GSAA_PROP_GSAAT040_EX_KEY,
                        GCConstGlobals.GSAA_PROP_GSAAT040_SCHEDULE_ID,
                        GCConstGlobals.GSAA_PROP_GSAAT040_EX_KEY,
                        GCConstGlobals.GSAA_PROP_GSAAT050_SCHEDULE_ID,
                        GCConstGlobals.GSAA_PROP_GSAAT050_EX_KEY
                ));

        GSAXS020Form outForm = new GSAXS020Form();

        model.addAttribute("form", outForm);
        return "GSAXS020";
    }
}
