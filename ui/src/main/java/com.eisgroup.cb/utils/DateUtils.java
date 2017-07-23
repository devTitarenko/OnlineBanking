package com.eisgroup.cb.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static Date startOfTheDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getTommorowDate(Date date){
        if (date!=null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, 1);
            return cal.getTime();
        }
        return null;
    }
}