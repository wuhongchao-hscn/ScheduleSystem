package com.hitachi.schedule.controller.handler.gsaa;

import com.hitachi.schedule.controller.actionform.GSAAS010Form;
import com.hitachi.schedule.controller.checker.GSAAS010Checker;
import com.hitachi.schedule.controller.common.GCConstGlobals;
import com.hitachi.schedule.controller.common.SessionUtil;
import com.hitachi.schedule.controller.component.CommonUtil;
import com.hitachi.schedule.controller.param.HmInfo;
import com.hitachi.schedule.controller.param.YmdInfo;
import com.hitachi.schedule.mybatis.pojo.Schedule;
import com.hitachi.schedule.service.GSAAScheduleF;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;

@Controller
@Slf4j
public class GSAAS010Handler {
    @Autowired
    private GSAAScheduleF gsaaService;
    @Autowired
    private GSAAS010Checker checker;
    @Autowired
    private CommonUtil commonUtil;

    private static final String RTN_STR_NG = "GSAAS010";
    private static final String RTN_STR_OK = "redirect:/GSAAS020Display";

    @PostMapping("/GSAAS010Register")
    public String GSAAS010Register(
            @Valid GSAAS010Form form,
            BindingResult br,
            Model model,
            RedirectAttributes redirectModel,
            HttpServletRequest request) {
        commonUtil.doInitGSAAS010(form);
        if (checker.formCheck(model, form, br)) {
            return RTN_STR_NG;
        }
        if (checker.gmCheck(model, form)) {
            return RTN_STR_NG;
        }

        String loginUserId = SessionUtil.getUserId(request);

        doInsert(form, loginUserId);

        return doInsertPro(redirectModel);
    }

    @PostMapping("/GSAAS010Update")
    public String GSAAS010Update(
            @Valid GSAAS010Form form,
            BindingResult br,
            Model model,
            RedirectAttributes redirectModel,
            HttpServletRequest request) {
        commonUtil.doInitGSAAS010(form);
        if (checker.formCheck(model, form, br)) {
            return RTN_STR_NG;
        }
        if (checker.gmCheck(model, form)) {
            return RTN_STR_NG;
        }
        String scheduleId = SessionUtil.getSessionValueString(
                request,
                GCConstGlobals.GSAA_PROP_GSAAT040_SCHEDULE_ID
        );
        int ex_key_session = SessionUtil.getSessionValueInt(
                request,
                GCConstGlobals.GSAA_PROP_GSAAT040_EX_KEY
        );

        if (doUpdatePre(model, request, form, scheduleId, ex_key_session)) {
            return RTN_STR_NG;
        }

        String loginUserId = SessionUtil.getUserId(request);
        doUpdate(form, scheduleId, loginUserId, ex_key_session);

        doUpdatePro(request, redirectModel);
        return RTN_STR_OK;
    }

    @PostMapping("/GSAAS010Back")
    public String GSAAS010Back(
            HttpServletRequest request,
            RedirectAttributes redirectModel) {
        SessionUtil.removeSessionValue(request,
                Arrays.asList(
                        GCConstGlobals.GSAA_PROP_GSAAT040_SCHEDULE_ID,
                        GCConstGlobals.GSAA_PROP_GSAAT040_EX_KEY
                ));
        redirectModel.addFlashAttribute("timedelta", 0);
        return RTN_STR_OK;
    }

    private void doInsert(GSAAS010Form form, String loginUserId) {

        Schedule schedule = new Schedule();
        YmdInfo yyk_ymd = new YmdInfo(form.getStrScheduleYykYmdY(),
                form.getStrScheduleYykYmdM(),
                form.getStrScheduleYykYmdD());
        schedule.setSchedule_yyk_ymd(yyk_ymd.toString());
        HmInfo start_hm = new HmInfo(
                form.getStrScheduleStartHmH(),
                form.getStrScheduleStartHmM()
        );
        schedule.setSchedule_start_hm(start_hm.toString());
        HmInfo end_hm = new HmInfo(
                form.getStrScheduleEndHmH(),
                form.getStrScheduleEndHmM()
        );
        schedule.setSchedule_end_hm(end_hm.toString());
        schedule.setSchedule_yukn(form.getStrScheduleYukn());
        schedule.setKigst_id(form.getStrKigstName());
        schedule.setSchedule_bku(form.getStrScheduleBku());
        schedule.setSchedule_ex_key(GCConstGlobals.GS_PROP_INIT_EX_KEY);
        schedule.setSchedule_delete_flag(GCConstGlobals.GSAA_PROP_GSAAT050_SEARCH_FLG);
        schedule.setUser_id(loginUserId);

        gsaaService.insertSchedule(schedule);
    }

    private String doInsertPro(RedirectAttributes redirectModel) {
        redirectModel.addFlashAttribute("timedelta", 0);
        return RTN_STR_OK;
    }

    private boolean doUpdatePre(
            Model model,
            HttpServletRequest request,
            GSAAS010Form form,
            String scheduleId,
            int ex_key_session) {

        if (!gsaaService.isExistSchedule(scheduleId)) {
            commonUtil.doRtnPre(model, form, "GSAAM004E");
            return true;
        }
        int ex_key_db = gsaaService.getScheduleExKeyById(scheduleId);

        if (ex_key_session != ex_key_db) {
            commonUtil.doRtnPre(model, form, "GSAAM008E");
            return true;
        }
        return false;
    }

    private void doUpdate(GSAAS010Form form, String scheduleId, String userId, int ex_key_session) {

        Schedule schedule = new Schedule();
        schedule.setSchedule_id(scheduleId);
        YmdInfo yyk_ymd = new YmdInfo(
                form.getStrScheduleYykYmdY(),
                form.getStrScheduleYykYmdM(),
                form.getStrScheduleYykYmdD()
        );
        schedule.setSchedule_yyk_ymd(yyk_ymd.toString());
        HmInfo start_hm = new HmInfo(
                form.getStrScheduleStartHmH(),
                form.getStrScheduleStartHmM()
        );
        schedule.setSchedule_start_hm(start_hm.toString());
        HmInfo end_hm = new HmInfo(
                form.getStrScheduleEndHmH(),
                form.getStrScheduleEndHmM()
        );
        schedule.setSchedule_end_hm(end_hm.toString());
        schedule.setSchedule_yukn(form.getStrScheduleYukn());
        schedule.setKigst_id(form.getStrKigstName());
        schedule.setSchedule_bku(form.getStrScheduleBku());
        schedule.setSchedule_ex_key(ex_key_session == 999 ? 0 : ex_key_session + 1);
        schedule.setSchedule_delete_flag(GCConstGlobals.GSAA_PROP_GSAAT050_SEARCH_FLG);
        schedule.setUser_id(userId);

        gsaaService.updateSchedule(schedule);
    }

    private void doUpdatePro(HttpServletRequest request, RedirectAttributes redirectModel) {
        SessionUtil.removeSessionValue(request,
                Arrays.asList(
                        GCConstGlobals.GSAA_PROP_GSAAT040_SCHEDULE_ID,
                        GCConstGlobals.GSAA_PROP_GSAAT040_EX_KEY
                ));
        redirectModel.addFlashAttribute("timedelta", 0);
    }

}
