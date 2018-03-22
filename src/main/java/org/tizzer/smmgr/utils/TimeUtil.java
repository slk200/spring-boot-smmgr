package org.tizzer.smmgr.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    public static Date endOfDay(Date date) {
        String strDate = new SimpleDateFormat("yyyy-MM-dd").format(date) + " 23:59:59";
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strDate);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date startOfDay(Date date) {
        String strDate = new SimpleDateFormat("yyyy-MM-dd").format(date) + " 00:00:00";
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strDate);
        } catch (ParseException e) {
            return null;
        }
    }

}
