package ru.skyeng.listening.Modules.AudioFiles.model;

import android.os.Bundle;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.skyeng.listening.CommonComponents.Interfaces.ModelData;
import ru.skyeng.listening.Modules.Categories.model.AudioTag;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 17/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class SubtitlesData implements ModelData<SubtitleFile> {

    private List<SubtitleFile> subtitles;

    public SubtitlesData() {
    }
    @Override
    public List<SubtitleFile> getPrimaryData() {
        return subtitles;
    }

    @Override
    public Map<String, String> getMetaData() {
        return null;
    }
}
