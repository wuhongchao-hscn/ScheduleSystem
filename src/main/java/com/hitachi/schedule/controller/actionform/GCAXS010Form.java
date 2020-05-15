package com.hitachi.schedule.controller.actionform;


import lombok.Data;

@Data
public class GCAXS010Form extends BaseForm {
    private String errLevel;
    private String errScreenId;
    private String errScreenName;
    private String errMsg;
    private String errorTimeDate;
    private String contMsg;
}
