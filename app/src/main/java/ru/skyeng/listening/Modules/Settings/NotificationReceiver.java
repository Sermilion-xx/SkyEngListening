package ru.skyeng.listening.Modules.Settings;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import ru.skyeng.listening.Modules.AudioFiles.AudioListActivity;
import ru.skyeng.listening.Modules.Settings.model.RemindTime;
import ru.skyeng.listening.Modules.Settings.model.SettingsObject;
import ru.skyeng.listening.R;
import ru.skyeng.listening.Utility.FacadePreferences;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 19/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class NotificationReceiver extends BroadcastReceiver {

    public static String ACTION_APP_NOTIFY = "ru.skyeng.listening.skyengNotification";

    public static final int REQUEST_CODE = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ACTION_APP_NOTIFY)) {

            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            boolean isWeekday = ((day >= Calendar.MONDAY) && (day <= Calendar.FRIDAY));

            Intent i = new Intent(context, NotificationService.class);
            SettingsObject settingsObject = FacadePreferences.getSettingsFromPref();

            boolean onEveryday = settingsObject.getRemindEvery() == RemindTime.EVERYDAY;
            boolean onWeekends = settingsObject.getRemindEvery() == RemindTime.WEEKENDS;
            boolean onWeekdays = !onEveryday && !onWeekends;
            boolean onDay = !onEveryday && !onWeekends;

            if(onEveryday
                    || (onWeekdays && isWeekday)
                    || (onWeekends && !isWeekday)
                    || (onDay && day == settingsObject.getTime().get(Calendar.DAY_OF_WEEK))){
                context.startService(i);
            }
        }

    }

}
