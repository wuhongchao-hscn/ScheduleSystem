package com.hitachi.schedule.controller.converter;

import com.hitachi.schedule.config.component.CommonUtil;
import com.hitachi.schedule.controller.actionform.GSAAS030Form;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GSAAS030Converter {
    @Autowired
    private CommonUtil commonUtil;

    public void convert(GSAAS030Form form) {
        form.setStrScheduleYykYmd(commonUtil.ymdConvert(form.getStrScheduleYykYmd()));

        form.setStrScheduleStartHm(commonUtil.hmConver(form.getStrScheduleStartHm()));

        form.setStrScheduleEndHm(commonUtil.hmConver(form.getStrScheduleEndHm()));

    }
}
