package ru.skyeng.listening.Modules.AudioFiles.model;

import ru.skyeng.listening.CommonComponents.Interfaces.RequestParams;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 17/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class SubtitlesRequestParams implements RequestParams {

    private int audioId;

    public SubtitlesRequestParams(int audioId) {
        this.audioId = audioId;
    }

    public int getAudioId() {
        return audioId;
    }

    public void setAudioId(int audioId) {
        this.audioId = audioId;
    }
}
