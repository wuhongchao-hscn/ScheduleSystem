package com.hitachi.schedule.controller.param;

import lombok.Data;

@Data
public class UserDetialInfoList extends UserInfoList {
    private String userImg;
    private String strRlId;
    private String strAllRlName;
    private int user_ex_key;
}
