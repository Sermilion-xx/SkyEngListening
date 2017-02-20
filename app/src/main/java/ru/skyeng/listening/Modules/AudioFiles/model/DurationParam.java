package ru.skyeng.listening.Modules.AudioFiles.model;

import android.support.v4.util.Pair;

import java.util.List;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 20/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class DurationParam {

    private List<Pair<Integer, Integer>> durations;

    public DurationParam(List<Pair<Integer, Integer>> durations) {
        this.durations = durations;
    }

    public DurationParam(){

    }

    public void clear(){
        durations.clear();
    }

    public int size(){
        return this.durations.size();
    }

    public void add(Pair<Integer, Integer> pair){
        this.durations.add(pair);
    }

    public boolean contains(Pair<Integer, Integer> pair){
        return this.durations.contains(pair);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if(durations!=null) {
            for (int i = 0; i < durations.size(); i++) {
                Pair<Integer, Integer> pair = durations.get(i);
                for (int j = 0; j < 2; j++) {
                    if (j == 0) {
                        stringBuilder.append("durations[").append(i).append("][").append(j).append("]=").append(pair.first).append("");
                    } else {
                        stringBuilder.append("durations[").append(i).append("][").append(j).append("]=").append(pair.second).append("");
                    }

                    stringBuilder.append("&");
                }
            }
            stringBuilder.delete(0,9);
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        }
        return stringBuilder.length()==0?"":stringBuilder.toString();
    }
}
