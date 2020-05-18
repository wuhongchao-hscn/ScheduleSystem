package com.hitachi.schedule.controller.param;

import lombok.Data;

@Data
public class ArticleListInfo {

    private long articleId;

    private long titleId;

    private String articleTitle;

    private String articleImg;

    private String articleContent;

    private boolean contentFlg = false;

    private long articleAgree;

    private Boolean articleAgreeFlg;

    private long articleCount;

}
