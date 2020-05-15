package com.hitachi.schedule.controller.actionform;

import com.hitachi.schedule.controller.param.SelectInfo;
import lombok.Data;

import java.util.List;

@Data
public class GSACS010Form extends BaseForm {

    private String strUserId;

    private String strShkinId;

    private String strShkinSmi;

    private String strKnskShbtCode;

    private String strTabFlag;

    private List<SelectInfo> listKnskShbt;
}
