package ru.skyeng.listening.Modules.Settings.model;

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
    private boolean allAccents;
    private final int americanAccentId;
    private boolean intAccent;
    private final int intAccentId;
    private boolean britishAccent;
    private final int britishAccentId;
    private boolean americanAccent;


    public SettingsObject(){
        remainderOn = false;
        level = 1;
        allAccents = true;
        intAccent = true;
        intAccentId = 0;
        britishAccent = true;
        britishAccentId = 2;
        americanAccent = true;
        americanAccentId = 1;
    }

    public int getAmericanAccentId() {
        return americanAccentId;
    }

    public int getIntAccentId() {
        return intAccentId;
    }

    public int getBritishAccentId() {
        return britishAccentId;
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
        return allAccents;
    }

    public void setAllAccents(boolean allAccents) {
        this.allAccents = allAccents;
        if(allAccents) {
            intAccent = true;
            britishAccent = true;
            americanAccent = true;
        }else {
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
        this.allAccents = false;
    }

    public boolean isBritishAccent() {
        return britishAccent;
    }

    public void setBritishAccent(boolean britishAccent) {
        this.britishAccent = britishAccent;
        this.allAccents = false;
    }

    public boolean isAmericanAccent() {
        return americanAccent;
    }

    public void setAmericanAccent(boolean americanAccent) {
        this.americanAccent = americanAccent;
        this.allAccents = false;
    }
}
