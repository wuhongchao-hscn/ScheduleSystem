package com.hitachi.schedule.controller.handler.gsaa;

import com.hitachi.schedule.controller.actionform.GSAAS010Form;
import com.hitachi.schedule.controller.actionform.GSAAS020Form;
import com.hitachi.schedule.controller.common.GCConstGlobals;
import com.hitachi.schedule.controller.common.SessionUtil;
import com.hitachi.schedule.controller.component.MessageReadUtil;
import com.hitachi.schedule.controller.param.YmdInfo;
import com.hitachi.schedule.service.GSAAScheduleF;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class GSAAS020Handler {
    @Autowired
    private MessageReadUtil messageUtil;
    @Autowired
    private GSAAScheduleF gsaaService;

    @PostMapping("/GSAAS020Today")
    public String GSAAS020Today(HttpServletRequest request) {
        return "redirect:GSAAS020Display";
    }


    @PostMapping("/GSAAS020PreviousDay")
    public String GSAAS020PreviousDay(RedirectAttributes redirectModel) {
        redirectModel.addFlashAttribute("timedelta", -1);
        return "redirect:GSAAS020Display";
    }


    @PostMapping("/GSAAS020NextDay")
    public String GSAAS020NextDay(RedirectAttributes redirectModel) {
        redirectModel.addFlashAttribute("timedelta", 1);
        return "redirect:GSAAS020Display";
    }


    @PostMapping("/GSAAS020Register")
    public String GSAAS020Register(
            RedirectAttributes redirectModel,
            HttpServletRequest request) {

        GSAAS010Form outForm = new GSAAS010Form();

        String searchKey = SessionUtil.getSessionValueString(
                request,
                GCConstGlobals.GSAA_PROP_GSAAT020_KNSK_JUKN
        );

        YmdInfo yyk_ymd = YmdInfo.init(searchKey);
        outForm.setStrScheduleYykYmdY(yyk_ymd.getYyyy());
        outForm.setStrScheduleYykYmdM(yyk_ymd.getMm());
        outForm.setStrScheduleYykYmdD(yyk_ymd.getDd());

        outForm.setStrKigstName("1");

        redirectModel.addFlashAttribute("form", outForm);
        return "redirect:GSAAS010Display";
    }


    @GetMapping("/GSAAS020Update")
    public String GSAAS020Update(
            String scheduleId,
            GSAAS020Form form,
            RedirectAttributes redirectModel,
            HttpServletRequest request) {

        if (!gsaaService.isExistSchedule(scheduleId)) {
            String errMsg = messageUtil.getMessage("GSAAM004E");
            redirectModel.addFlashAttribute("timedelta", 0);
            redirectModel.addFlashAttribute("error", errMsg);
            return "redirect:GSAAS020Display";
        }

        SessionUtil.saveSessionValue(
                request,
                GCConstGlobals.GSAA_PROP_GSAAT040_SCHEDULE_ID,
                scheduleId
        );

        return "redirect:GSAAS010Display";
    }


    @GetMapping("/GSAAS020Delete")
    public String GSAAS020Delete(
            String scheduleId,
            GSAAS020Form form,
            RedirectAttributes redirectModel,
            HttpServletRequest request) {

        if (!gsaaService.isExistSchedule(scheduleId)) {
            String errMsg = messageUtil.getMessage("GSAAM004E");
            redirectModel.addFlashAttribute("timedelta", 0);
            redirectModel.addFlashAttribute("error", errMsg);
            return "redirect:GSAAS020Display";
        }

        SessionUtil.saveSessionValue(
                request,
                GCConstGlobals.GSAA_PROP_GSAAT050_SCHEDULE_ID,
                scheduleId
        );
        return "redirect:GSAAS030Display";
    }
}
