package ru.skyeng.listening.Modules.Settings.model;

import java.util.ArrayList;
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

    private boolean remainderOn;
    private int level;
    private Set<Integer> accentIds;
    private boolean intAccent;
    private boolean britishAccent;
    private boolean americanAccent;


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
