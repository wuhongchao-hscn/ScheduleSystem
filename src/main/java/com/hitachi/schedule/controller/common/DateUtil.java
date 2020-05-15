package com.hitachi.schedule.controller.common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static String getSysDateYmd() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(GCConstGlobals.GS_FORMAT_STORE_YMD);
        return LocalDate.now().format(dtf);
    }

    public static String calculateDay(String searchKey, Object timedelta) {
        Integer calDay = Integer.parseInt(timedelta.toString());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(GCConstGlobals.GS_FORMAT_STORE_YMD);
        LocalDate searchKeyT = LocalDate.parse(searchKey, dtf);

        searchKeyT = searchKeyT.plusDays(calDay);

        return searchKeyT.format(dtf);
    }
}
