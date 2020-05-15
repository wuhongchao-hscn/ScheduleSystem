package com.hitachi.schedule.mybatis.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private String user_id;
    private String shkin_id;
    private String user_password;
    private byte[] user_image;
    private String user_image_name;
    private String user_delete_flag;
    private String user_update_uid;
    private String user_update_ymd;
    private int user_ex_key;
    private int user_login_key;
    private Shkin shkin;
}
