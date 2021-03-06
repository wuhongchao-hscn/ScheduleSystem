package com.hitachi.schedule.dao.mybatis.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Menu implements Serializable {
    private String gyoumu_name;
    private String href_url;
    private String div_name;
    private String dt_name;
    private String dd_value;
    private int sort_order;
}
