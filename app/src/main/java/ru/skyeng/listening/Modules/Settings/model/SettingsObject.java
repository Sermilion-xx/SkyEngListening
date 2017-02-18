package ru.skyeng.listening.Modules.Settings.model;

import android.util.SparseIntArray;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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

    private boolean remainderOn;
    private int level;
    private Set<Integer> accentIds;
    private boolean intAccent;
    private boolean britishAccent;
    private boolean americanAccent;
    private int remainderDays;
    private SparseIntArray duration;
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
        remainderDays = 7;
        time = defaultCalendar();
        duration = new SparseIntArray(4);
        duration.put(0,5);
    }

    public static Calendar defaultCalendar() {
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(Calendar.HOUR, 12);
        currentDate.set(Calendar.MINUTE, 0);
        return currentDate;
    }

    public void setDuration(boolean[] index) {
        int one = 0;
        int two = 5;
        for(int i=0; i<index.length; i++){
            if(index[i]){
                this.duration.put(one,two);
                one = two;
                two = one*2;
                if(i==3){
                    two = 360;
                }
            }
        }
    }

    public SparseIntArray getDuration(){
        return duration;
    }

    public boolean[] getDurationsBooleanArray() {
        int[] keys = new int[]{0,5,10,20};
        boolean[] values = new boolean[4];
        for(int i:keys){
            if(duration.get(i, -1)!=-1){
                values[i] = true;
            }
        }
        return values;
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
