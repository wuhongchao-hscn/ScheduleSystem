package com.hitachi.schedule.controller.actionform;

import com.hitachi.schedule.controller.configuration.GamenInfoConfig;
import com.hitachi.schedule.controller.param.NavInfo;
import lombok.Data;

import java.util.List;

@Data
public class BaseForm {
    private String screenName;

    private String screenId;

    private String errItemId;

    private List<NavInfo> navList;


    public BaseForm() {
        this.screenId = Thread.currentThread().getStackTrace()[2].getFileName().substring(0, 8);
        this.screenName = GamenInfoConfig.getScreenNameById(this.screenId);
        this.navList = GamenInfoConfig.getNavInfoListById(this.screenId);
    }
}
