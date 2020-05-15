package com.hitachi.schedule.controller.param;

import lombok.Data;

@Data
public class UserHeadInfo {

    private String ssk_name;
    private String shjin_smi;
    private String roles;
    private String rolesName;
    private byte[] userImg;
}
