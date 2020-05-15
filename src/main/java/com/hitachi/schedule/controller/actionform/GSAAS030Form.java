package com.hitachi.schedule.controller.actionform;

import lombok.Data;

@Data
public class GSAAS030Form extends BaseForm {
    private String strScheduleYykYmd;

    private String strScheduleStartHm;

    private String strScheduleEndHm;

    private String strScheduleYukn;

    private String strKigstName;

    private String strScheduleBku;
}
