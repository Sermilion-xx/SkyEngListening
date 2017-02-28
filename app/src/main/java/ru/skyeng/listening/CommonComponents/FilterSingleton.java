package ru.skyeng.listening.CommonComponents;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.List;

import ru.skyeng.listening.Modules.AudioFiles.AudioListActivity;
import ru.skyeng.listening.R;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 22/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class FilterSingleton {

    private static final int START_1 = 0;
    private static final int END_1 = 300;
    private static final int START_2 = 300;
    private static final int END_2 = 600;
    private static final int START_3 = 600;
    private static final int END_3 = 1200;
    private static final int START_4 = 1200;
    private static final int END_4 = 2400;
    private static final int START_5 = 2400;
    private static final int END_5 = 60000;

    private static FilterSingleton INSTANCE;

    private List<Integer> selectedTags;
    private Pair<Integer, Integer> duration;

    public static FilterSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FilterSingleton();
        }
        return INSTANCE;
    }

    private FilterSingleton() {
        selectedTags = new ArrayList<>();
    }

    public List<Integer> getSelectedTags() {
        return selectedTags;
    }

    public void setSelectedTags(List<Integer> selectedTags) {
        this.selectedTags = selectedTags;
    }

    public Pair<Integer, Integer> getDuration() {
        return duration;
    }

    public void setDuration(Pair<Integer, Integer> duration) {
        this.duration = duration;
    }

    public static String getDurationText(Context context, Pair<Integer, Integer> durationRange){
            if (durationRange.first == 0 && durationRange.second == 2400) {
                return context.getString(R.string.length);
            } else {
                if (durationRange.first == 0 && durationRange.second < 2400) {
                    return String.format(context.getString(R.string.from_0_to), durationRange.second / 60);
                } else if (durationRange.first > 0 && durationRange.second == 2400) {
                    return String.format(context.getString(R.string.from_n_and_greater), durationRange.first / 60);
                }
                if (durationRange.first > 0 && durationRange.second < 2400) {
                    return String.format(context.getString(R.string.selected_time), durationRange.first / 60, durationRange.second / 60);
                }
            }
        return context.getString(R.string.length);
    }
}
