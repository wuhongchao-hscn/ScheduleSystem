package com.hitachi.schedule.service.impl;

import com.hitachi.schedule.config.common.DateUtil;
import com.hitachi.schedule.config.common.GXConst;
import com.hitachi.schedule.controller.param.ScheduleInfoList;
import com.hitachi.schedule.controller.param.SelectInfo;
import com.hitachi.schedule.dao.mybatis.mapper.xml.KigstMapper;
import com.hitachi.schedule.dao.mybatis.mapper.xml.ScheduleMapper;
import com.hitachi.schedule.dao.mybatis.pojo.Kigst;
import com.hitachi.schedule.dao.mybatis.pojo.Schedule;
import com.hitachi.schedule.service.GSAAScheduleF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames = GXConst.GSAA_CACHE_NAME)
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
    @Cacheable
    public Schedule getScheduleById(String schedule_id) {
        return scheduleMapper.findScheduleById(schedule_id);
    }

    @Override
    public List<ScheduleInfoList> getScheduleListByDate(String schedule_yyk_ymd) {
        List<Schedule> scheduleList = scheduleMapper.findScheduleListByDate(schedule_yyk_ymd);
        List<ScheduleInfoList> scheduleInfoList = new ArrayList<>();
        Map<String, String> rtn_obj = getKigstNameMap();
        for (Schedule schedule : scheduleList) {
            ScheduleInfoList info = new ScheduleInfoList();
            info.setCheckRadio(schedule.getSchedule_id());
            String strKigstName = rtn_obj.get(schedule.getKigst_id());
            if (strKigstName.length() > 18) {
                info.setStrKigstName(strKigstName.substring(0, 18)
                        + GXConst.GSAA_FUGO_SCHEDULE_LIST_DISPLAY
                );
            } else {
                info.setStrKigstName(strKigstName);
            }
            info.setStrScheduleStartHM(schedule.getSchedule_start_hm());
            info.setStrScheduleEndHM(schedule.getSchedule_end_hm());
            String schedule_yukn = schedule.getSchedule_yukn();
            if (schedule_yukn.length() > 24) {
                info.setStrScheduleYukn(schedule_yukn.substring(0, 24)
                        + GXConst.GSAA_FUGO_SCHEDULE_LIST_DISPLAY
                );
            } else {
                info.setStrScheduleYukn(schedule_yukn);
            }
            scheduleInfoList.add(info);
        }
        return scheduleInfoList;
    }

    @Override
    public int getScheduleExKeyById(String schedule_id) {
        return scheduleMapper.getScheduleExKeyById(schedule_id);
    }

    @Override
    @CachePut(key = "#result.schedule_id", condition = "#schedule.schedule_delete_flag == '0'")
    @CacheEvict(key = "#schedule.schedule_id", condition = "#schedule.schedule_delete_flag != '0'")
    public Schedule updateSchedule(Schedule schedule) {
        schedule.setSchedule_update_uid(schedule.getUser_id());
        schedule.setSchedule_update_ymd(DateUtil.getSysDateYmd());
        scheduleMapper.updateSchedule(schedule);
        return schedule;
    }

    @Override
    @CachePut(key = "#schedule.schedule_id")
    public Schedule insertSchedule(Schedule schedule) {
        schedule.setSchedule_update_uid(schedule.getUser_id());
        schedule.setSchedule_update_ymd(DateUtil.getSysDateYmd());
        scheduleMapper.insertSchedule(schedule);
        return schedule;
    }

    @Override
    @CachePut(key = "'kigst_'+#p0")
    public String getKigstNameById(String kigst_id) {
        return kigstMapper.findKigstNameById(kigst_id);
    }

    @Override
    @Cacheable(key = "'kigsts'")
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
