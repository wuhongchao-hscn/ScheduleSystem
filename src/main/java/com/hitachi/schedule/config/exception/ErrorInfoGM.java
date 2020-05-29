package com.hitachi.schedule.config.exception;

import lombok.Data;

@Data
public class ErrorInfoGM extends RuntimeException {

    // エラー画面id
    private String errScreenId;

    //错误信息
    private String errMsg;

    //错误信息
    private boolean msgGetFlg;

    public ErrorInfoGM(String errScreenId, String errMsg) {
        this.errScreenId = errScreenId;
        this.errMsg = errMsg;
        this.msgGetFlg = true;
    }

    public ErrorInfoGM(String errScreenId, String errMsg, boolean msgGetFlg) {
        this.errScreenId = errScreenId;
        this.errMsg = errMsg;
        this.msgGetFlg = msgGetFlg;
    }
}