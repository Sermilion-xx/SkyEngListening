package ru.skyeng.listening.AudioFiles.domain;

import android.content.Intent;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class AudioData {

    @SerializedName("data")
    private List<AudioFile> audioFiles;
    private Map<String, String> meta;

    public AudioData(List<AudioFile> audioFiles, Map<String, String> meta) {
        this.audioFiles = audioFiles;
        this.meta = meta;
    }

    public List<AudioFile> getAudioFiles() {
        return audioFiles;
    }

    public void setAudioFiles(List<AudioFile> audioFiles) {
        this.audioFiles = audioFiles;
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }
}
