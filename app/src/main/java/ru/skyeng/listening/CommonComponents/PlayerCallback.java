package ru.skyeng.listening.CommonComponents;

import ru.skyeng.listening.Modules.AudioFiles.model.AudioFile;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 22/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public interface PlayerCallback {
    void startPlaying(AudioFile audioFile);
    void pausePlayer();
    void continuePlaying();
}
