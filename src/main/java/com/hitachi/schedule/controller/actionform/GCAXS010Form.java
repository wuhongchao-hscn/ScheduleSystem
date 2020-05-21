package com.hitachi.schedule.controller.actionform;


import lombok.Data;

@Data
public class GCAXS010Form extends BaseForm {
    private String errLevel;
    private String errScreenIdLabel = "エラーコード";
    private String errScreenId;
    private String errScreenNameLabel = "エラー内容";
    private String errScreenName;
    private String errMsg;
    private String errorTimeDate;
    private String contMsg;
}
