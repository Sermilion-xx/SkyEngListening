package ru.skyeng.listening.Modules.Settings.model;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.skyeng.listening.R;

import static android.content.Context.NOTIFICATION_SERVICE;

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
    private List<Map<Integer, Integer>> duration;
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
        duration =new ArrayList<>();
        Map<Integer, Integer> defaultDuration = new HashMap<>();
        defaultDuration.put(0, 100);
        duration.add(defaultDuration);
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
                Map<Integer, Integer> aList = new HashMap<>();
                aList.put(one, two);
                duration.add(aList);
            }
            one = two;
            two = one*2;
            if(i==3){
                two = 360;
            }
        }
    }

    public List<Map<Integer, Integer>> getDuration() {
        return duration;
    }

    public boolean[] getDurationsBooleanArray() {
        int[] keys = new int[]{0 ,5*60, 10*60, 20*60};
        boolean[] values = new boolean[4];
        for(int i=0; i<duration.size(); i++){
            Map<Integer, Integer> map = duration.get(i);
            for(int j = 0; j<keys.length; j++){
                if(map.containsKey(keys[j])){
                    values[j] = true;
                }
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

    public void setDuration(List<Map<Integer, Integer>> duration) {
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
