package com.hitachi.schedule.dao.mybatis.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRl implements Serializable {
    private String rl_id;
    private String user_id;
    private String user_rl_update_uid;
    private String user_rl_update_ymd;
    private int user_rl_ex_key;
    private String rl_roles;
    private User user;
}
