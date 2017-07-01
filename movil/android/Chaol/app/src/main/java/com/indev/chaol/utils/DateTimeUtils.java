package com.indev.chaol.utils;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by jvier on 22/03/2017.
 */

public class DateTimeUtils {

    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";

    public static CalendarDay getCalendarDay(String myDate) {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            cal.setTime(sdf.parse(myDate));// all done
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return CalendarDay.from(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DATE));
    }

    public static String getParseTimeStamp(Long date) {
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                .format(new java.util.Date(date * 1000L));
    }


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

    private static String getRandomString(final int sizeOfRandomString) {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }
}
