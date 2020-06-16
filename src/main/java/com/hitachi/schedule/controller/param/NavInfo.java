package com.hitachi.schedule.controller.param;

import com.hitachi.schedule.config.configuration.GamenInfoConfig;
import lombok.Data;

import java.io.Serializable;

@Data
public class NavInfo implements Serializable {
    private String hrefUrl;
    private String hrefText;

    public NavInfo(String hrefUrl, String hrefText) {
        this.hrefUrl = hrefUrl;
        this.hrefText = GamenInfoConfig.getScreenNameById(hrefText);
    }
}
