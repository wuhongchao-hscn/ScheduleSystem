package com.hitachi.schedule.controller.checker;

import com.hitachi.schedule.controller.actionform.GSAAS010Form;
import com.hitachi.schedule.controller.component.CommonUtil;
import com.hitachi.schedule.controller.exception.ErrorInfoGM;
import com.hitachi.schedule.controller.param.HmInfo;
import com.hitachi.schedule.controller.param.YmdInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@Primary
public class GSAAS010Checker extends CommonChecker {
    @Autowired
    private CommonUtil commonUtil;

    public boolean gmCheck(
            Model model,
            GSAAS010Form form) {
        log.info("業務チェックを行います。");
        try {
            do_check_yyk_ymd(form);
            do_check_start_end_hm(form);

        } catch (ErrorInfoGM e) {
            form.setErrItemId(e.getErrScreenId());
            commonUtil.doRtnPre(model, form, e.getErrMsg());
            return true;
        }
        log.info("項目チェックには間違いがない。");
        return false;
    }

    private void do_check_yyk_ymd(GSAAS010Form form) {
        YmdInfo yyk_ymd = new YmdInfo(
                form.getStrScheduleYykYmdY(),
                form.getStrScheduleYykYmdM(),
                form.getStrScheduleYykYmdD()
        );
        List<String> item_id = Arrays.asList(
                "strScheduleYykYmdY",
                "strScheduleYykYmdM",
                "strScheduleYykYmdD");
        ErrorInfoGM e_in = new ErrorInfoGM(
                commonUtil.getStringFromList(item_id), "GSAAM001E"
        );
        commonUtil.checkDateExist(yyk_ymd, e_in);


    }

    private void do_check_start_end_hm(GSAAS010Form form) {
        HmInfo start_hm = new HmInfo(
                form.getStrScheduleStartHmH(),
                form.getStrScheduleStartHmM()
        );
        HmInfo end_hm = new HmInfo(
                form.getStrScheduleEndHmH(),
                form.getStrScheduleEndHmM()
        );
        List<String> item_id = Arrays.asList(
                "strScheduleStartHmH",
                "strScheduleStartHmM",
                "strScheduleEndHmH",
                "strScheduleEndHmM");
        ErrorInfoGM e_in = new ErrorInfoGM(
                String.join(",", item_id),
                "GSAAM002E"
        );

        commonUtil.checkStartEndHmOk(start_hm, end_hm, e_in);
    }
}
