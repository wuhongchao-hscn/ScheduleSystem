package com.hitachi.schedule.controller.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class ArticleDetialInfo implements Serializable {

    private long articleId;

    private long titleId;

    private String articleTitle;

    private String articleImg;

    private String userImg;

    private String userName;

    private String articleContent;

    private boolean contentFlg = false;

    private long articleAgree;

    private int articleAgreeFlg;

    private boolean articleLikeFlg = false;

    private long articleCount;

}
