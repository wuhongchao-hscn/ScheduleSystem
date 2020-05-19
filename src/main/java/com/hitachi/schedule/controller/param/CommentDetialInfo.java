package com.hitachi.schedule.controller.param;

import lombok.Data;

@Data
public class CommentDetialInfo {
    private long id;

    private byte[] imgData;

    private String userId;

    private String userName;

    private String dateLong;

    private long level;

    private String comment;
}
