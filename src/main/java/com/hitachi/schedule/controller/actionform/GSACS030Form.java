package com.hitachi.schedule.controller.actionform;

import com.hitachi.schedule.controller.param.UserDetialInfoList;
import lombok.Data;

import java.util.List;

@Data
public class GSACS030Form extends BaseForm {

    private List<UserDetialInfoList> userDetialList;

    private boolean updateFlg = true;

}
