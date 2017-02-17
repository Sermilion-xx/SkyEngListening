package ru.skyeng.listening.CommonComponents;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public static String getDateFromMillis(long millis) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss", Locale.getDefault());
        return formatter.format(new Date(millis));
    }

    public static long dateToMills(String inputString) {
        try {
            String local = inputString.substring(0,8);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = sdf.parse("1970-01-01 " + inputString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
