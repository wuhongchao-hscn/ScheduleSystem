package com.hitachi.schedule.controller.param;

import com.hitachi.schedule.controller.common.StringUtil;
import lombok.Data;

@Data
public class HmInfo {
    private String hh;
    private String mm;

    public HmInfo(String hh, String mm) {
        this.hh = hh;
        this.mm = mm;
    }

    public static HmInfo init(String hhmm) {
        HmInfo obj = null;
        if (null != hhmm && hhmm.length() == 4) {
            obj = new HmInfo(
                    hhmm.substring(0, 2),
                    hhmm.substring(2, 4)
            );
        }
        return obj;
    }

    @Override
    public String toString() {
        return StringUtil.padZeroOnL(hh, 2) +
                StringUtil.padZeroOnL(mm, 2);
    }
}
