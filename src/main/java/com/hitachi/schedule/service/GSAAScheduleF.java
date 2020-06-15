package com.hitachi.schedule.service;

import com.hitachi.schedule.controller.param.ScheduleInfoList;
import com.hitachi.schedule.controller.param.SelectInfo;
import com.hitachi.schedule.dao.mybatis.pojo.Schedule;

import java.util.List;

public interface GSAAScheduleF {
    boolean isExistSchedule(String schedule_id);

    Schedule getScheduleById(String schedule_id);

    List<ScheduleInfoList> getScheduleListByDate(String schedule_yyk_ymd);

    int getScheduleExKeyById(String schedule_id);

    Schedule updateSchedule(Schedule schedule);

    String insertSchedule(Schedule schedule);

    String getKigstNameById(String kigst_id);

    List<SelectInfo> getKigstList();
}
