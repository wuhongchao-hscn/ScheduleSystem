package com.hitachi.schedule.service.param;

import lombok.Data;

@Data
public class UserFindParam {

    private String strUserId;

    private String strShkinId;

    private String strShkinSmi;

    private String strKnskShbtCode;

    private Integer startNo;

    private Integer endNo;

    private Integer allPage;

    private String strTabFlag;

    private String errItemId;

    private String deleteUserIdForm;

    private String deleteUserId;
}
