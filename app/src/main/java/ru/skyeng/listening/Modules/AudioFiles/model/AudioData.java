package ru.skyeng.listening.Modules.AudioFiles.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.skyeng.listening.CommonComponents.Interfaces.ModelData;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class AudioData implements ModelData<AudioFile>{

    @SerializedName("data")
    private List<AudioFile> audioFiles;
    private Map<String, String> meta;

    public AudioData(List<AudioFile> audioFiles, Map<String, String> meta) {
        this.audioFiles = audioFiles;
        this.meta = meta;
    }

    public AudioData(){
        this.audioFiles = new ArrayList<>();
    }

    @Override
    public List<AudioFile> getPrimaryData() {
        return audioFiles;
    }

    @Override
    public Map<String, String> getMetaData() {
        return meta;
    }
}
