package ru.skyeng.listening.Modules.Settings.model;

import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 17/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class SettingsObject {

    private int level;
    private Set<Integer> accentIds;
    private boolean intAccent;
    private boolean britishAccent;
    private boolean americanAccent;

    private boolean remainderOn;
    //0 - weekends, 1 - weekdays, 2 - everyday
    //3,4,5,6,7,8,9 - mon - sun
    private int remindEvery;
    private List<Integer> duration;
    private Calendar time;


    public SettingsObject() {
        accentIds = new HashSet<>();
        remainderOn = false;
        level = 1;
        intAccent = true; //5
        britishAccent = true; //4
        americanAccent = true; //3
        accentIds.add(2);
        accentIds.add(4);
        accentIds.add(3);
        time = setNotificationTime(12, 0);
        duration = new ArrayList<>();

    }

    public static Calendar setNotificationTime(int hours, int minutes) {
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(Calendar.HOUR, hours);
        currentDate.set(Calendar.MINUTE, minutes);
        return currentDate;
    }

    public void setDuration(boolean[] index) {
        duration.clear();
        int one = 0;
        int two = 5*60;
        for(int i=0; i<index.length; i++){
            if(index[i]){
                duration.add(one);
                duration.add(two);
            }else {
                duration.add(-1);
                duration.add(-1);
            }
            one = two;
            two = one*2;
            if(i==3){
                two = 360;
            }
        }
    }

    public List<Integer> getDuration() {
        return duration;
    }

    public boolean[] getDurationsBooleanArray() {
        boolean allFalse = true;
        SparseIntArray map = new SparseIntArray();
        //durations: 0, 300, 300, 600, 600, 1200, 1200, 2400
        map.put(0, 300);
        map.put(300, 600);
        map.put(600, 1200);
        map.put(1200, 2400);
        boolean[] values = new boolean[4];
        int index1 = 0;
        for(int i=0; i<duration.size(); i++){
            if(i+1<duration.size()) {
                if (map.get(duration.get(index1)) == duration.get(i + 1)) {
                    if(i<values.length) {
                        values[i] = true;
                        allFalse = false;
                    }
                }
                index1++;
            }
        }
        if(allFalse){
            for(int i = 0; i<values.length; i++){
                values[i] = false;
            }
        }
        return values;
    }

    public void setAccentIds(Set<Integer> accentIds) {
        this.accentIds = accentIds;
    }

    public int getRemindEvery() {
        return remindEvery;
    }

    public void setRemindEvery(int remindEvery) {
        this.remindEvery = remindEvery;
    }

    public void setDuration(List<Integer> duration) {
        this.duration = duration;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(int hours, int minutes) {
        this.time = setNotificationTime(hours, minutes);
    }

    public Set<Integer> getAccentIds() {
        return accentIds;
    }

    public boolean isRemainderOn() {
        return remainderOn;
    }

    public void setRemainderOn(boolean remainderOn) {
        this.remainderOn = remainderOn;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isAllAccents() {
        return accentIds.size()==3;
    }

    public void setAllAccents(boolean allAccents) {;
        if (allAccents) {
            intAccent = true;
            britishAccent = true;
            americanAccent = true;
            accentIds.clear();
            accentIds.add(5);
            accentIds.add(4);
            accentIds.add(3);
        } else {
            accentIds.clear();
            intAccent = false;
            britishAccent = false;
            americanAccent = false;
        }
    }

    public boolean isIntAccent() {
        return intAccent;
    }

    public void setIntAccent(boolean intAccent) {
        this.intAccent = intAccent;
        if (!intAccent) {
            accentIds.remove(Integer.valueOf(5));
        }else {
            accentIds.add(5);
        }
    }

    public boolean isBritishAccent() {
        return britishAccent;
    }

    public void setBritishAccent(boolean britishAccent) {
        this.britishAccent = britishAccent;
        if (britishAccent) {
            accentIds.add(4);
        }else {
            accentIds.remove(Integer.valueOf(4));
        }
    }

    public boolean isAmericanAccent() {
        return americanAccent;
    }

    public void setAmericanAccent(boolean americanAccent) {
        this.americanAccent = americanAccent;
        if(americanAccent){
            accentIds.add(3);
        }else {
            accentIds.remove(Integer.valueOf(3));
        }
    }



}
