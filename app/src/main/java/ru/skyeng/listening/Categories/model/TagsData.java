package ru.skyeng.listening.Categories.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

import ru.skyeng.listening.CommonComponents.Interfaces.ModelData;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 15/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class TagsData implements ModelData<AudioTag>{

    @SerializedName("data")
    private List<AudioTag> audioTags;
    private Map<String, String> meta;

    public TagsData(List<AudioTag> audioTags, Map<String, String> meta) {
        this.audioTags = audioTags;
        this.meta = meta;
    }

    @Override
    public List<AudioTag> getPrimaryData() {
        return audioTags;
    }

    @Override
    public Map<String, String> getMetaData() {
        return meta;
    }
}
