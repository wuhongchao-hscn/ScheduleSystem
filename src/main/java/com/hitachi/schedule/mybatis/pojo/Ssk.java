package com.hitachi.schedule.mybatis.pojo;

import lombok.Data;

import java.util.List;

@Data
public class Ssk {
    private String ssk_id;
    private String jui_ssk_id;
    private String ssk_name;
    private String ssk_update_uid;
    private String ssk_update_ymd;
    private int ssk_ex_key;
    private List<Ssk> children;
}
