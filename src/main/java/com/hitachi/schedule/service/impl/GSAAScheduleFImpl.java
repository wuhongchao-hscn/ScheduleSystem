package com.hitachi.schedule.service.impl;

import com.hitachi.schedule.config.common.DateUtil;
import com.hitachi.schedule.config.common.GCConstGlobals;
import com.hitachi.schedule.controller.param.ScheduleInfoList;
import com.hitachi.schedule.controller.param.SelectInfo;
import com.hitachi.schedule.dao.mybatis.mapper.xml.KigstMapper;
import com.hitachi.schedule.dao.mybatis.mapper.xml.ScheduleMapper;
import com.hitachi.schedule.dao.mybatis.pojo.Kigst;
import com.hitachi.schedule.dao.mybatis.pojo.Schedule;
import com.hitachi.schedule.service.GSAAScheduleF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GSAAScheduleFImpl implements GSAAScheduleF {
    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private KigstMapper kigstMapper;

    @Override
    public boolean isExistSchedule(String schedule_id) {
        return scheduleMapper.findScheduleCount(schedule_id) > 0;
    }

    @Override
    public Schedule getScheduleById(String schedule_id) {
        return scheduleMapper.findScheduleById(schedule_id);
    }

    @Override
    public List<ScheduleInfoList> getScheduleListByDate(String schedule_yyk_ymd) {
        List<Schedule> db_rtn = scheduleMapper.findScheduleListByDate(schedule_yyk_ymd);
        List<ScheduleInfoList> schedule_form = new ArrayList<>();
        Map<String, String> rtn_obj = getKigstNameMap();
        for (Schedule obj : db_rtn) {
            ScheduleInfoList obj_p = new ScheduleInfoList();
            obj_p.setCheckRadio(obj.getSchedule_id());
            String strKigstName = rtn_obj.get(obj.getKigst_id());
            if (strKigstName.length() > 18) {
                obj_p.setStrKigstName(strKigstName.substring(0, 18)
                        + GCConstGlobals.GSAA_FUGO_SCHEDULE_LIST_DISPLAY
                );
            } else {
                obj_p.setStrKigstName(strKigstName);
            }
            obj_p.setStrScheduleStartHM(obj.getSchedule_start_hm());
            obj_p.setStrScheduleEndHM(obj.getSchedule_end_hm());
            String schedule_yukn = obj.getSchedule_yukn();
            if (schedule_yukn.length() > 24) {
                obj_p.setStrScheduleYukn(schedule_yukn.substring(0, 24)
                        + GCConstGlobals.GSAA_FUGO_SCHEDULE_LIST_DISPLAY
                );
            } else {
                obj_p.setStrScheduleYukn(schedule_yukn);
            }
            schedule_form.add(obj_p);
        }
        return schedule_form;
    }

    @Override
    public int getScheduleExKeyById(String schedule_id) {
        return scheduleMapper.getScheduleExKeyById(schedule_id);
    }

    @Override
    public void updateSchedule(Schedule schedule) {
        schedule.setSchedule_update_uid(schedule.getUser_id());
        schedule.setSchedule_update_ymd(DateUtil.getSysDateYmd());
        scheduleMapper.updateSchedule(schedule);
    }

    @Override
    public String insertSchedule(Schedule schedule) {
        schedule.setSchedule_update_uid(schedule.getUser_id());
        schedule.setSchedule_update_ymd(DateUtil.getSysDateYmd());
        scheduleMapper.insertSchedule(schedule);
        return schedule.getSchedule_id();
    }

    @Override
    public String getKigstNameById(String kigst_id) {
        return kigstMapper.findKigstNameById(kigst_id);
    }

    @Override
    public List<SelectInfo> getKigstList() {
        List<Kigst> db_rtn = kigstMapper.getAllKigst();
        List<SelectInfo> rtn_obj = new ArrayList<>();
        rtn_obj.add(new SelectInfo("", "(未選択)"));
        for (Kigst obj : db_rtn) {
            rtn_obj.add(new SelectInfo(obj.getKigst_id(), obj.getKigst_name()));
        }
        return rtn_obj;
    }

    private Map<String, String> getKigstNameMap() {

        List<Kigst> db_rtn = kigstMapper.getAllKigst();
        Map<String, String> rtn_obj = new HashMap<>();
        for (Kigst obj : db_rtn) {
            rtn_obj.put(obj.getKigst_id(), obj.getKigst_name());
        }
        return rtn_obj;
    }
}
