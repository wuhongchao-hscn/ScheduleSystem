package com.hitachi.schedule.controller.param;

import lombok.Data;

@Data
public class TitleInfo {

    private long titleId;

    private String titleName;

    private String titleContent;

    private boolean contentFlg = false;
}
