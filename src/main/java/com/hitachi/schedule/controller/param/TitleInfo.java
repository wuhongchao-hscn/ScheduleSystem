package com.hitachi.schedule.controller.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class TitleInfo implements Serializable {

    private long titleId;

    private String titleName;

    private String titleContent;

    private boolean contentFlg = false;
}
