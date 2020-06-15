package com.hitachi.schedule.config.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtil {

    public static String getSysDateYmd() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(GXConst.GS_FORMAT_STORE_YMD);
        return LocalDate.now().format(dtf);
    }

    public static String calculateDay(String searchKey, Object timedelta) {
        Integer calDay = Integer.parseInt(timedelta.toString());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(GXConst.GS_FORMAT_STORE_YMD);
        LocalDate searchKeyT = LocalDate.parse(searchKey, dtf);

        searchKeyT = searchKeyT.plusDays(calDay);

        return searchKeyT.format(dtf);
    }

    public static String getDayBetween(Date fromDate, LocalDateTime lToDateTime) {
        LocalDateTime lFromDateTime = fromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        long years = ChronoUnit.YEARS.between(lFromDateTime, lToDateTime);
        if (years > 0) {
            return years + "年前";
        }

        long months = ChronoUnit.MONTHS.between(lFromDateTime, lToDateTime);
        if (months > 0) {
            return months + "个月前";
        }

        long days = ChronoUnit.DAYS.between(lFromDateTime, lToDateTime);
        if (days > 0) {
            return days + "天前";
        }

        long hours = ChronoUnit.HOURS.between(lFromDateTime, lToDateTime);
        if (hours > 0) {
            return hours + "小时前";
        }

        long minutes = ChronoUnit.MINUTES.between(lFromDateTime, lToDateTime);
        if (minutes > 0) {
            return minutes + "分钟前";
        }

        return "刚刚";
    }
}
