package org.tizzer.smmgr.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    /**
     * 字符串转时间
     *
     * @param dateTime
     * @return
     */
    public static Date string2DateTime(String dateTime) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 字符串转日期
     *
     * @param date
     * @return
     */
    public static Date string2Date(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

}
