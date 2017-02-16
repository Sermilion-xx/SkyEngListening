package ru.skyeng.listening.AudioFiles.model;

import java.util.List;

import ru.skyeng.listening.CommonComponents.Interfaces.RequestParams;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class AudioFilesRequestParams implements RequestParams {

    private Integer page;
    private Integer pageSize;
    private String title;
    private Integer accentId;
    private Integer levelId;
    private List<Integer> tagIds;
    private Integer durationGT;
    private Integer durationLT;

    public AudioFilesRequestParams(){

    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getAccentId() {
        return accentId;
    }

    public void setAccentId(Integer accentId) {
        this.accentId = accentId;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public List<Integer> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Integer> tagIds) {
        this.tagIds = tagIds;
    }

    public Integer getDurationGT() {
        return durationGT;
    }

    public void setDurationGT(Integer durationGT) {
        this.durationGT = durationGT;
    }

    public Integer getDurationLT() {
        return durationLT;
    }

    public void setDurationLT(Integer durationLT) {
        this.durationLT = durationLT;
    }
}
