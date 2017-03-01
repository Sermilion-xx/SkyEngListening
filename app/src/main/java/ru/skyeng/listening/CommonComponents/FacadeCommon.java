package ru.skyeng.listening.CommonComponents;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 12/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class FacadeCommon {

    private static final String SART_DATE_STR = "1970-01-01 ";
    private static final String TIME_ZONE = "UTC";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SS";
    private static final String LOCALE = "Ru";

    public static String getDateFromMillis(long millis) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss", Locale.getDefault());
        return formatter.format(new Date(millis));
    }

    public static long dateToMills(int year, int month, int day, int hour, int minute, int seconds) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, hour, minute, seconds);

        return c.getTimeInMillis();
    }

    public static long dateToMills(String inputString) {
        try {
            String local = inputString.substring(0,8);
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, new Locale(LOCALE));
            sdf.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
            Date date = sdf.parse(SART_DATE_STR + inputString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
