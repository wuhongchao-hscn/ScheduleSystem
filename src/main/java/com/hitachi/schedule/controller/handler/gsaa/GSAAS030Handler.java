package com.hitachi.schedule.controller.handler.gsaa;

import com.hitachi.schedule.config.common.GCConstGlobals;
import com.hitachi.schedule.config.common.SessionUtil;
import com.hitachi.schedule.config.component.CommonUtil;
import com.hitachi.schedule.controller.actionform.GSAAS030Form;
import com.hitachi.schedule.dao.mybatis.pojo.Schedule;
import com.hitachi.schedule.service.GSAAScheduleF;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Controller
@Slf4j
public class GSAAS030Handler {
    @Autowired
    private GSAAScheduleF gsaaService;
    @Autowired
    private CommonUtil commonUtil;

    private static final String RTN_STR_NG = "redirect:/GSAAS030Display";
    private static final String RTN_STR_OK = "redirect:/GSAAS020Display";

    @PostMapping("/GSAAS030Delete")
    public String GSAAS030Delete(
            GSAAS030Form form,
            RedirectAttributes redirectModel,
            HttpServletRequest request) {
        String scheduleId = SessionUtil.getSessionValueString(
                request,
                GCConstGlobals.GSAA_PROP_GSAAT050_SCHEDULE_ID
        );
        int ex_key_session = SessionUtil.getSessionValueInt(
                request,
                GCConstGlobals.GSAA_PROP_GSAAT050_EX_KEY
        );

        String rtn = doDeletePre(redirectModel, form, request, scheduleId, ex_key_session);
        if (!StringUtils.isEmpty(rtn)) {
            return rtn;
        }

        String loginUserId = SessionUtil.getUserId(request);
        doDelete(scheduleId, loginUserId, ex_key_session);

        doDeletePro(request, redirectModel);
        return RTN_STR_OK;
    }

    @PostMapping("/GSAAS030Back")
    public String GSAAS030Back(
            HttpServletRequest request,
            RedirectAttributes redirectModel) {
        SessionUtil.removeSessionValue(request,
                Arrays.asList(
                        GCConstGlobals.GSAA_PROP_GSAAT050_SCHEDULE_ID,
                        GCConstGlobals.GSAA_PROP_GSAAT050_EX_KEY
                ));
        redirectModel.addFlashAttribute("timedelta", 0);
        return RTN_STR_OK;
    }

    private String doDeletePre(
            RedirectAttributes redirectModel,
            GSAAS030Form form,
            HttpServletRequest request,
            String scheduleId,
            int ex_key_session) {

        if (!gsaaService.isExistSchedule(scheduleId)) {
            commonUtil.doRtnPre(redirectModel, form, "GSAAM004E");
            return RTN_STR_OK;
        }
        int ex_key_db = gsaaService.getScheduleExKeyById(scheduleId);

        if (ex_key_session != ex_key_db) {
            commonUtil.doRtnPre(redirectModel, form, "GSAAM008E");
            return RTN_STR_NG;
        }
        return null;
    }

    private void doDelete(String scheduleId, String userId, int ex_key_db) {
        Schedule schedule = new Schedule();
        schedule.setSchedule_id(scheduleId);
        schedule.setSchedule_ex_key(ex_key_db == 999 ? 0 : ex_key_db + 1);
        schedule.setUser_id(userId);
        schedule.setSchedule_delete_flag(GCConstGlobals.GSAA_PROP_GSAAT050_DELETE_FLG);
        gsaaService.updateSchedule(schedule);
    }

    private void doDeletePro(HttpServletRequest request, RedirectAttributes redirectModel) {
        SessionUtil.removeSessionValue(request,
                Arrays.asList(
                        GCConstGlobals.GSAA_PROP_GSAAT050_SCHEDULE_ID,
                        GCConstGlobals.GSAA_PROP_GSAAT050_EX_KEY
                ));
        redirectModel.addFlashAttribute("timedelta", 0);
    }
}
