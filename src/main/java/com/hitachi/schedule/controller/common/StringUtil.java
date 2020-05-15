package com.hitachi.schedule.controller.common;

import java.util.regex.Pattern;

public class StringUtil {
    public static String padZeroOnL(String value, int length) {
        return padStringLeft(value, length, "0");
    }

    private static String padStringLeft(String value, int length, String fugo) {
        int needLength = length - value.length();
        StringBuilder sb = new StringBuilder();
        while (needLength > 0) {
            sb.append(fugo);
            needLength--;
        }
        return sb.append(value).toString();
    }

    public static boolean fullCharCheck(String value) {
        String regText = "[^ -~｡-ﾟ]+";
        Pattern pattern = Pattern.compile(regText);
        return pattern.matcher(value).matches();
    }

    public static boolean eiSuJiCheck(String value) {
        String regText = "^[0-9a-zA-Z]+$";
        Pattern pattern = Pattern.compile(regText);
        return pattern.matcher(value).matches();
    }


}
