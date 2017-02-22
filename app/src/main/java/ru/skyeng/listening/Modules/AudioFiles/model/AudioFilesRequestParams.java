package ru.skyeng.listening.Modules.AudioFiles.model;

import android.support.v4.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class AudioFilesRequestParams {

    private Integer page;
    private Integer pageSize;
    private String title;
    private List<Integer> accentIds;
    private Integer levelId;
    private List<Integer> tagIds;
    private Integer durationGT;
    private Integer durationLT;
    private Map<String, Integer> duration;

    public AudioFilesRequestParams() {
        this.duration = new HashMap<>();
        this.page = 1;
        this.pageSize = 15;
    }

    public Map<String, Integer> getDuration() {
        return duration;
    }

    public void setDuration(Map<String, Integer> duration) {
        this.duration = duration;
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

    public List<Integer> getAccentIds() {
        return accentIds;
    }

    public void setAccentIds(List<Integer> accentIds) {
        this.accentIds = accentIds;
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

    public void prepareDurations(List<Pair<Integer, Integer>> durationValues) {
        Map<String, Integer> paramsMap = new HashMap<>();
        for (int i = 0; i < durationValues.size(); i++) {
            if (durationValues.get(i).first > 0 && durationValues.get(i).second > 0) {
                paramsMap.put("durations[" + i + "][0]", durationValues.get(i).first);
                paramsMap.put("durations[" + i + "][1]", durationValues.get(i).second);
            }
        }
        setDuration(paramsMap);
    }
}
