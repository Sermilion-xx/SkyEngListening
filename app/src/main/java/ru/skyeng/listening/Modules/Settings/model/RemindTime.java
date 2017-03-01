package ru.skyeng.listening.Modules.Settings.model;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 23/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public enum RemindTime{

    WEEKENDS(0), WEEKDAYS(1), EVERYDAY(2), MONDAY(3), TUESDAY(4), WEDNESDAY(5), THURSDAY(6), FRIDAY(7), SATURDAY(8), SUNDAY(9);
    int value;
    RemindTime(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }



}
