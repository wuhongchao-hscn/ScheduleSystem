package com.hitachi.schedule.controller.param;

import lombok.Data;

@Data
public class ArticleDetialInfo {

    private long articleId;

    private long titleId;

    private String articleTitle;

    private String articleImg;

    private String articleContent;

    private boolean contentFlg = false;

    private long articleAgree;

    private int articleAgreeFlg;

    private long articleCount;

}
