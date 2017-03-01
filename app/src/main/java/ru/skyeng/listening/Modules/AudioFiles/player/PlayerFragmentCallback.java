package ru.skyeng.listening.Modules.AudioFiles.player;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 27/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public interface PlayerFragmentCallback {

    void seekTo(long time);

    void updateCover();
}
