package com.hitachi.schedule.dao.mybatis.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Code implements Serializable {
    private int id;
    private String code_bnri;
    private String code_key;
    private String code_value;
}
