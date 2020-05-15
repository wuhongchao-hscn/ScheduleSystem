package com.hitachi.schedule.controller.converter;

import com.hitachi.schedule.controller.actionform.GSAAS020Form;
import com.hitachi.schedule.controller.component.CommonUtil;
import com.hitachi.schedule.controller.param.ScheduleInfoList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GSAAS020Converter {
    @Autowired
    private CommonUtil commonUtil;

    public void convert(GSAAS020Form form) {
        log.info("コンバートします。");

        form.setStrCurrentYmd(commonUtil.ymdConvert(form.getStrCurrentYmd()));

        for (ScheduleInfoList obj : form.getScheduleList()) {
            obj.setStrScheduleStartHM(commonUtil.hmConver(obj.getStrScheduleStartHM()));
            obj.setStrScheduleEndHM(commonUtil.hmConver(obj.getStrScheduleEndHM()));
        }

    }
}
