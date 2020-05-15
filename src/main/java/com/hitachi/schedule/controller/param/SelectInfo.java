package com.hitachi.schedule.controller.param;

import lombok.Data;

@Data
public class SelectInfo {
    private String key;
    private String value;

    public SelectInfo(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
