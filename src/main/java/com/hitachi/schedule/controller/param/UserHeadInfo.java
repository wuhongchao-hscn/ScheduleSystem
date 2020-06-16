package com.hitachi.schedule.controller.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserHeadInfo implements Serializable {

    private String ssk_name;
    private String shjin_smi;
    private String roles;
    private String rolesName;
    private String userImg;
}
