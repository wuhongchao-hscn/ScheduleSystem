package com.hitachi.schedule.controller.actionform;

import com.hitachi.schedule.controller.param.UserInfoList;
import lombok.Data;

import java.util.List;

@Data
public class GSACS020Form extends BaseForm {

    private String strUserId;

    private String strShkinId;

    private String strShkinSmi;

    private String strKnskShbtCode;

    private List<String> pageList;

    private String pageNow;

    private String hitRangeLabel;

    private List<UserInfoList> userList;
    
    private String deleteUserId;
}
