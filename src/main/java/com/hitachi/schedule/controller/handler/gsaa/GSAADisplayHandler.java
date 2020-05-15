package com.hitachi.schedule.controller.handler.gsaa;

import com.hitachi.schedule.controller.actionform.GSAAS010Form;
import com.hitachi.schedule.controller.actionform.GSAAS020Form;
import com.hitachi.schedule.controller.actionform.GSAAS030Form;
import com.hitachi.schedule.controller.common.DateUtil;
import com.hitachi.schedule.controller.common.GCConstGlobals;
import com.hitachi.schedule.controller.common.SessionUtil;
import com.hitachi.schedule.controller.component.CommonUtil;
import com.hitachi.schedule.controller.component.MessageReadUtil;
import com.hitachi.schedule.controller.converter.GSAAS020Converter;
import com.hitachi.schedule.controller.converter.GSAAS030Converter;
import com.hitachi.schedule.controller.param.HmInfo;
import com.hitachi.schedule.controller.param.ScheduleInfoList;
import com.hitachi.schedule.controller.param.YmdInfo;
import com.hitachi.schedule.mybatis.pojo.Schedule;
import com.hitachi.schedule.service.GSAAScheduleF;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
@Slf4j
public class GSAADisplayHandler {
    @Autowired
    private MessageReadUtil messageUtil;
    @Autowired
    private GSAAScheduleF gsaaService;
    @Autowired
    GSAAS020Converter gsaas020Converter;
    @Autowired
    GSAAS030Converter gsaas030Converter;
    @Autowired
    private CommonUtil commonUtil;

    @GetMapping("/GSAAS010Display")
    public String GSAAS010Display(
            Model model,
            HttpServletRequest request) {
        Object inForm = model.getAttribute("form");
        GSAAS010Form outForm = null;
        if (null != inForm) {
            outForm = (GSAAS010Form) inForm;
        } else {
            outForm = new GSAAS010Form();
        }
        commonUtil.doInitGSAAS010(outForm);

        String scheduleId = SessionUtil.getSessionValueString(
                request,
                GCConstGlobals.GSAA_PROP_GSAAT040_SCHEDULE_ID);

        if (!StringUtils.isEmpty(scheduleId)) {
            outForm.setInsertFlg(false);

            Schedule schedule = gsaaService.getScheduleById(scheduleId);

            // 年月日
            YmdInfo yyk_ymd = YmdInfo.init(schedule.getSchedule_yyk_ymd());
            outForm.setStrScheduleYykYmdY(yyk_ymd.getYyyy());
            outForm.setStrScheduleYykYmdM(yyk_ymd.getMm());
            outForm.setStrScheduleYykYmdD(yyk_ymd.getDd());
            // 開始時刻
            HmInfo strScheduleStartHm = HmInfo.init(schedule.getSchedule_start_hm());
            outForm.setStrScheduleStartHmH(strScheduleStartHm.getHh());
            outForm.setStrScheduleStartHmM(strScheduleStartHm.getMm());
            // 終了時刻
            HmInfo strScheduleEndHm = HmInfo.init(schedule.getSchedule_end_hm());
            outForm.setStrScheduleEndHmH(strScheduleEndHm.getHh());
            outForm.setStrScheduleEndHmM(strScheduleEndHm.getMm());
            // 用件
            outForm.setStrScheduleYukn(schedule.getSchedule_yukn());
            // 会議室
            outForm.setStrKigstName(schedule.getKigst_id());
            // 備考
            outForm.setStrScheduleBku(schedule.getSchedule_bku());

            SessionUtil.saveSessionValue(
                    request,
                    GCConstGlobals.GSAA_PROP_GSAAT040_EX_KEY,
                    schedule.getSchedule_ex_key()
            );
        }

        model.addAttribute("form", outForm);

        log.info("スケジュール登録更新画面を表示します。");
        return "GSAAS010";
    }

    @GetMapping("/GSAAS020Display")
    public String GSAAS020Display(Model model, HttpServletRequest request) {
        GSAAS020Form outForm = new GSAAS020Form();

        String searchKey = SessionUtil.getSessionValueString(
                request,
                GCConstGlobals.GSAA_PROP_GSAAT020_KNSK_JUKN
        );

        Object timedelta = model.getAttribute("timedelta");
        if (null != timedelta) {
            searchKey = DateUtil.calculateDay(searchKey, timedelta);
        } else {
            searchKey = DateUtil.getSysDateYmd();
        }

        SessionUtil.saveSessionValue(
                request,
                GCConstGlobals.GSAA_PROP_GSAAT020_KNSK_JUKN,
                searchKey
        );

        outForm.setStrCurrentYmd(searchKey);
        List<ScheduleInfoList> scheduleList = gsaaService.getScheduleListByDate(searchKey);
        outForm.setScheduleList(scheduleList);

        gsaas020Converter.convert(outForm);

        model.addAttribute("form", outForm);

        log.info("スケジュール一覧画面を表示します。");
        return "GSAAS020";
    }

    @GetMapping("/GSAAS030Display")
    public String GSAAS030Display(Model model, HttpServletRequest request) {
        GSAAS030Form outForm = new GSAAS030Form();

        String scheduleId = SessionUtil.getSessionValueString(
                request,
                GCConstGlobals.GSAA_PROP_GSAAT050_SCHEDULE_ID
        );

        Schedule schedule = gsaaService.getScheduleById(scheduleId);
        // 年月日
        outForm.setStrScheduleYykYmd(schedule.getSchedule_yyk_ymd().toString());
        // 開始時刻
        outForm.setStrScheduleStartHm(schedule.getSchedule_start_hm());
        // 終了時刻
        outForm.setStrScheduleEndHm(schedule.getSchedule_end_hm());
        // 用件
        outForm.setStrScheduleYukn(schedule.getSchedule_yukn());
        // 会議室
        outForm.setStrKigstName(gsaaService.getKigstNameById(schedule.getKigst_id()));
        // 備考
        outForm.setStrScheduleBku(schedule.getSchedule_bku());

        SessionUtil.saveSessionValue(
                request,
                GCConstGlobals.GSAA_PROP_GSAAT050_EX_KEY,
                schedule.getSchedule_ex_key()
        );

        gsaas030Converter.convert(outForm);

        model.addAttribute("'infomation'", messageUtil.getMessage("GSACM006I"));

        model.addAttribute("form", outForm);

        log.info("スケジュール削除画面を表示します。");
        return "GSAAS030";
    }
}
