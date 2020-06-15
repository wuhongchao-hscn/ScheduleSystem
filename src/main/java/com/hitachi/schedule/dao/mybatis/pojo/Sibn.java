package com.hitachi.schedule.dao.mybatis.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Sibn implements Serializable {
    private String category;
    private String sequenceno;
}
