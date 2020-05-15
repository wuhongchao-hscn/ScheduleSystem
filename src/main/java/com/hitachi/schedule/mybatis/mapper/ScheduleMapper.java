package com.hitachi.schedule.mybatis.mapper;


import com.hitachi.schedule.mybatis.pojo.Schedule;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ScheduleMapper {
    public List<Schedule> findScheduleListByDate(String schedule_yyk_ymd);

    public Schedule findScheduleById(String schedule_id);

    public int findScheduleCount(String schedule_id);

    public int getScheduleExKeyById(String schedule_id);

    public void updateSchedule(Schedule schedule);

    public void insertSchedule(Schedule schedule);
}