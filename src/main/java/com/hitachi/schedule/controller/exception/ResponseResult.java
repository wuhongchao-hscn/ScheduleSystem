package com.hitachi.schedule.controller.exception;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseResult implements Serializable {

    private static final long serialVersionUID = 6054052582421291408L;

    private String message;
    private Object data;
    private String code;
    private boolean success;
    private Long total;

    public ResponseResult() {
    }

    public ResponseResult(boolean success, Object data) {
        this.success = success;
        this.data = data;
    }

    public ResponseResult(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
