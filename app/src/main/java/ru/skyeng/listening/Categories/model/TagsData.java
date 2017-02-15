package ru.skyeng.listening.Categories.model;

import java.util.List;
import java.util.Map;

import ru.skyeng.listening.CommonComponents.ModelData;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 15/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class TagsData implements ModelData<Tag>{

    private List<Tag> tags;
    private Map<String, String> meta;

    public TagsData(List<Tag> tags, Map<String, String> meta) {
        this.tags = tags;
        this.meta = meta;
    }

    @Override
    public List<Tag> getPrimaryData() {
        return tags;
    }

    @Override
    public Map<String, String> getMetaData() {
        return meta;
    }
}
