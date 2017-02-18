package ru.skyeng.listening.Utility.asynctask;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 18/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public interface Consumer<A> {
    void consume(A param);
}
