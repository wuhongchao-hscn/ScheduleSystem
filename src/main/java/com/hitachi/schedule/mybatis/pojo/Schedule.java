package com.hitachi.schedule.mybatis.pojo;

import lombok.Data;

@Data
public class Schedule {
    private String schedule_id;
    private String kigst_id;
    private String user_id;
    private String schedule_yyk_ymd;
    private String schedule_start_hm;
    private String schedule_end_hm;
    private String schedule_yukn;
    private String schedule_bku;
    private String schedule_delete_flag;
    private String schedule_update_uid;
    private String schedule_update_ymd;
    private int schedule_ex_key;
}
