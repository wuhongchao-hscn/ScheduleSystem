package com.hitachi.schedule.mybatis.pojo;

import lombok.Data;

@Data
public class Menu {
    private String gyoumu_name;
    private String href_url;
    private String div_name;
    private String dt_name;
    private String dd_value;
    private int sort_order;
}
