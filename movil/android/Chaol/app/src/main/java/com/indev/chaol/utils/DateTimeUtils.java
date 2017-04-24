package com.indev.chaol.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by jvier on 22/03/2017.
 */

public class DateTimeUtils {

    public static long  getTimeStamp() {
        return System.currentTimeMillis() / 1000L;
    }

    public static String getActualTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(c.getTime());
    }

    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH);
    }

    public static int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE);
    }

    public static String getDayName(int dayOfWeek) {
        String dayName = "";

        switch (dayOfWeek) {
            case Calendar.MONDAY:
                dayName = Constants.LUNES;
                break;
            case Calendar.TUESDAY:
                dayName = Constants.MARTES;
                break;
            case Calendar.WEDNESDAY:
                dayName = Constants.MIERCOLES;
                break;
            case Calendar.THURSDAY:
                dayName = Constants.JUEVES;
                break;
            case Calendar.FRIDAY:
                dayName = Constants.VIERNES;
                break;
            case Calendar.SATURDAY:
                dayName = Constants.SABADO;
                break;
            case Calendar.SUNDAY:
                dayName = Constants.DOMINGO;
                break;
        }

        return dayName;
    }
}
