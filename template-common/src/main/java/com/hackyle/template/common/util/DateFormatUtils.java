package com.hackyle.template.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtils {
    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DEFAULT_PATTERN);

    public static Date parseString(String dateStr, String pattern) throws ParseException {
        if(dateStr == null || "".equals(dateStr.trim())) {
            return null;
        }

        if(pattern == null || "".equals(pattern.trim())) {
            return parseString(dateStr);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.parse(dateStr);
    }

    public static Date parseString(String dateStr) throws ParseException {
        if(dateStr == null || "".equals(dateStr.trim())) {
            return null;
        }

        return SIMPLE_DATE_FORMAT.parse(dateStr);
    }
}
