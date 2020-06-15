package com.hitachi.schedule.dao.mybatis.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Kigst implements Serializable {
    private String kigst_id;
    private String kigst_name;
    private int kigst_shuyu_nnzu;
    private int kigst_ryukn;
    private String kigst_bhn;
    private String kigst_update_uid;
    private String kigst_update_ymd;
    private int kigst_ex_key;
}
