package com.hitachi.schedule.controller.param;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class UserInfoList {
    private String deleteFlg;
    private String userId;
    private String strShkinId;
    private String strShkinSmi;
    private String strSsk;

    public List<String> toList() {
        return Arrays.asList(userId, strShkinId, strShkinSmi, strSsk);
    }
}
