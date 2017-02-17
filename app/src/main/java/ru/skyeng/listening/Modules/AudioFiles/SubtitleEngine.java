package ru.skyeng.listening.Modules.AudioFiles;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ru.skyeng.listening.CommonComponents.FacadeCommon;
import ru.skyeng.listening.Modules.AudioFiles.model.SubtitleFile;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 17/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

class SubtitleEngine implements Comparable<Long> {

    private int index = -1;
    private List<SubtitleFile> subtitleFileList;
    private SubtitleFile current;

    int size() {
        if (subtitleFileList == null) return 0;
        return subtitleFileList.size();
    }

    SubtitleEngine() {
        subtitleFileList = new ArrayList<>();
    }

    public void clear() {
        subtitleFileList.clear();
    }

    SubtitleFile updateSubtitles(long currentTime) {
        int result = this.compareTo(currentTime);
        if (result == -1 || result == 0) {
            index++;
            current = subtitleFileList.get(index);
        }
        return current;
    }

    void setSubtitles(List<SubtitleFile> subtitleFileList) {
        this.subtitleFileList = subtitleFileList;
        current = subtitleFileList.get(0);
    }

    @Override
    public int compareTo(@NonNull Long o) {
        if (current == null) return -1;
        long thisStart = FacadeCommon.dateToMills(current.getEndTime()) * 1000;
        if (thisStart < o) {
            return -1;
        } else if (thisStart == 0) {
            return 0;
        } else {
            return 1;
        }
    }
}
