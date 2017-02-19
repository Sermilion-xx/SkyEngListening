package ru.skyeng.listening.Utility;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

import ru.skyeng.listening.Modules.AudioFiles.AudioListActivity;
import ru.skyeng.listening.R;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 19/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class NotificationService extends GcmTaskService {

    public static final String TAG_TASK_PERIODIC_LOG = "notificationTask";
    private static int NOTIFICATION_ID = 0;

    @Override
    public int onRunTask(TaskParams taskParams) {
        switch (taskParams.getTag()) {
            case TAG_TASK_PERIODIC_LOG:
                createNotification("Пора учить английский", this,  AudioListActivity.class);
                return GcmNetworkManager.RESULT_SUCCESS;
            default:
                return GcmNetworkManager.RESULT_FAILURE;
        }
    }

    public static void createNotification(String message, Context context, Class aClass) {
        Intent intent = new Intent(context, aClass);
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);
        Notification noti = new Notification.Builder(context)
                .setContentTitle("SkyEng Listening")
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent).build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(NOTIFICATION_ID, noti);
        NOTIFICATION_ID++;
    }
}
