package ru.skyeng.listening.CommonCoponents;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

}
