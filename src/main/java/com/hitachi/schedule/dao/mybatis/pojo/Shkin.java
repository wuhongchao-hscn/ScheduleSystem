package com.hitachi.schedule.dao.mybatis.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Shkin implements Serializable {
    private String shkin_id;
    private String ssk_id;
    private String shkin_smi;
    private String shkin_update_uid;
    private String shkin_update_ymd;
    private int shkin_ex_key;
}
