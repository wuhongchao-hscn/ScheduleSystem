package com.hitachi.schedule.controller.param;

import com.hitachi.schedule.config.common.StringUtil;
import lombok.Data;

import java.io.Serializable;

@Data
public class YmdInfo implements Serializable {
    private String yyyy;
    private String mm;
    private String dd;

    public YmdInfo(String yyyy, String mm, String dd) {
        this.yyyy = yyyy;
        this.mm = mm;
        this.dd = dd;
    }

    public static YmdInfo init(String yyyyMMdd) {
        YmdInfo obj = null;
        if (null != yyyyMMdd && yyyyMMdd.length() == 8) {
            obj = new YmdInfo(
                    yyyyMMdd.substring(0, 4),
                    yyyyMMdd.substring(4, 6),
                    yyyyMMdd.substring(6, 8)
            );
        }
        return obj;
    }

    @Override
    public String toString() {
        return StringUtil.padZeroOnL(yyyy, 4) +
                StringUtil.padZeroOnL(mm, 2) +
                StringUtil.padZeroOnL(dd, 2);
    }
}
