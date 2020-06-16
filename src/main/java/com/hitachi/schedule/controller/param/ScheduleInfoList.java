package com.hitachi.schedule.controller.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class ScheduleInfoList implements Serializable {
    private String checkRadio;
    private String strScheduleStartHM;
    private String strScheduleEndHM;
    private String strScheduleYukn;
    private String strKigstName;
}
