package com.hitachi.schedule.dao.mybatis.pojo;

import lombok.Data;

import java.util.List;

@Data
public class Rl {
    private String rl_id;
    private String rl_name;
    private String rl_update_uid;
    private String rl_update_ymd;
    private int rl_ex_key;
    private List<UserRl> user_rl_list;
}
