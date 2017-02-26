package ru.skyeng.listening.Utility.TagView;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 24/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */


import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

class TagViewUtils {

    static int dipToPx(Context c,float dipValue) {
        DisplayMetrics metrics = c.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }
}