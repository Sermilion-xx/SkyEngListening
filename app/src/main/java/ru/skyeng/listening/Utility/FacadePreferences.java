package ru.skyeng.listening.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import ru.skyeng.listening.CommonComponents.SEApplication;
import ru.skyeng.listening.Modules.Settings.model.SettingsObject;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 17/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class FacadePreferences {

    private static final String KEY_PREF_SETTINGS = "settings";

    public static SettingsObject getSettingsFromPref() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(SEApplication.getINSTANCE());
        Gson gson = new Gson();
        String json = pref.getString(KEY_PREF_SETTINGS, "");
        return gson.fromJson(json, SettingsObject.class);
    }

    public static void setSettingsToPref(SettingsObject settings) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(SEApplication.getINSTANCE()).edit();
        Gson gson = new Gson();
        String json = gson.toJson(settings);
        editor.putString(KEY_PREF_SETTINGS, json);
        editor.apply();
    }

}
