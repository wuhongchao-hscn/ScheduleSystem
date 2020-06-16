package com.hitachi.schedule.controller.param;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CommentDetialInfo implements Serializable {
    private long id;

    private byte[] imgData;

    private String userId;

    private String userImg;

    private String userName;

    private String dateLong;

    private long level;

    private long agree;

    private String comment;

    private List<CommentDetialInfo> childs;
}
