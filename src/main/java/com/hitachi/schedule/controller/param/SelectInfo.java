package com.hitachi.schedule.controller.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class SelectInfo implements Serializable {
    private String key;
    private String value;

    public SelectInfo() {
    }

    public SelectInfo(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
