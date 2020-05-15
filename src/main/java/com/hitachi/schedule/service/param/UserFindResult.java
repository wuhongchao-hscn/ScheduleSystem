package com.hitachi.schedule.service.param;

import com.hitachi.schedule.controller.param.UserInfoList;
import lombok.Data;

import java.util.List;

@Data
public class UserFindResult {

    private List<String> pageList;

    private String pageNow;

    private Integer startNo;

    private Integer endNo;

    private String hitRangeLabel;

    List<UserInfoList> userList;

}
