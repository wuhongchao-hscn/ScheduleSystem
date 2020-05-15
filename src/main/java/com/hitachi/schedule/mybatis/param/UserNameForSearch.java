package com.hitachi.schedule.mybatis.param;

import lombok.Data;

@Data
public class UserNameForSearch {
    private String shkin_id;

    private String shkin_smi;

    private String strKnskShbtCode;

    private Integer startNo;

    private Integer sizes;
}
