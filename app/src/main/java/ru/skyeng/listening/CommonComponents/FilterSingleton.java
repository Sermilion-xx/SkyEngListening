package ru.skyeng.listening.CommonComponents;

import android.support.v4.util.Pair;
import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.List;

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

    private static FilterSingleton INSTANCE;

    private List<Integer> selectedTags;
    private List<Pair<Integer, Integer>> duration;

    public static FilterSingleton getInstance() {
        if(INSTANCE == null){
            INSTANCE = new FilterSingleton();
        }
        return INSTANCE;
    }

    public void setDuration(boolean[] index) {
        boolean allZeros = true;
        duration.clear();
        int one = 0;
        int two = 5 * 60;
        for (int i = 0; i < index.length; i++) {
            if (index[i]) {
                duration.add(new Pair<>(one, two));
                allZeros = false;
            } else {
                duration.add(new Pair<>(0, 0));
            }
            one = two;
            two = one * 2;
            if (i == 3) {
                two = 360;
            }
        }
        if(allZeros){
            duration.clear();
            duration.add(new Pair<>(START_1,END_4));
        }
    }

    public boolean[] getDurationsBooleanArray() {
        boolean allFalse = true;
        SparseIntArray map = new SparseIntArray();
        //durations: 0, 300, 300, 600, 600, 1200, 1200, 2400
        map.put(START_1, END_1);
        map.put(START_2, END_2);
        map.put(START_3, END_3);
        map.put(START_4, END_4);
        boolean[] values = new boolean[4];
        for (int i = 0; i < duration.size(); i++) {
            if (map.get(duration.get(i).first) == duration.get(i).second) {
                if (i < values.length) {
                    values[i] = true;
                    allFalse = false;
                }
            }
        }
        if (allFalse) {
            for (int i = 0; i < values.length; i++) {
                values[i] = false;
            }
        }
        return values;
    }

    private FilterSingleton(){
        duration = new ArrayList<>();
        selectedTags = new ArrayList<>();
    }

    public List<Integer> getSelectedTags() {
        return selectedTags;
    }

    public void setSelectedTags(List<Integer> selectedTags) {
        this.selectedTags = selectedTags;
    }

    public List<Pair<Integer, Integer>> getDuration() {
        return duration;
    }

    public void setDuration(List<Pair<Integer, Integer>> duration) {
        this.duration = duration;
    }
}
