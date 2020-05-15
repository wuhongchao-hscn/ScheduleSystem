package com.hitachi.schedule.controller.actionform;

import com.hitachi.schedule.controller.param.ScheduleInfoList;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GSAAS020Form extends BaseForm {
    private String strCurrentYmd;

    private String scheduleId;

    private List<ScheduleInfoList> scheduleList = new ArrayList<>();
}
