package com.hitachi.schedule.controller.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class MenuInfo implements Serializable {
    private String hrefUrl;
    private String divName;
    private String dtName;
    private String ddValue;
    private boolean displayFlg;

}
