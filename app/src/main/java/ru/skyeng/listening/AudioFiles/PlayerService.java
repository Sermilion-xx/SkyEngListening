package ru.skyeng.listening.AudioFiles;

import android.app.Notification;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import java.io.IOException;

import ru.skyeng.listening.AudioFiles.domain.AudioFile;
import ru.skyeng.listening.R;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 13/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    public static final String ACTION_PLAY = "ru.skyeng.ACTION_PLAY";
    public static final String ACTION_PAUSE = "ru.skyeng.ACTION_PAUSE";
    public static final String ACTION_CONTINUE = "ru.skyeng.ACTION_CONTINUE";
    public static final String AUDIO_URL = "audioUrl";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_AUDIO_FILE = "audioFile";
    private AudioPlayer mPlayer;
    private AudioFile mAudioFile;

    private BroadcastReceiver mPlayerBroadcast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_PLAY)) {
                String audioUrl = intent.getStringExtra(AUDIO_URL);
                try {
                    if (mPlayer.isPaused() && mPlayer.getExtraData().equals(audioUrl)) {
                        mPlayer.setState(1);
                        mPlayer.play(audioUrl);
                    } else {
                        mPlayer.play(audioUrl);
                        mPlayer.setExtraData(audioUrl);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (intent.getAction().equals(ACTION_PAUSE)) {
                mPlayer.pause();
            }else if(intent.getAction().equals(ACTION_CONTINUE)){
                mPlayer.play();
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        final IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_PAUSE);
        filter.addAction(ACTION_PLAY);
        filter.addAction(ACTION_CONTINUE);
        this.registerReceiver(mPlayerBroadcast, filter);
        mPlayer = new AudioPlayer(this);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals(ACTION_PLAY)) {
            mAudioFile = intent.getParcelableExtra(EXTRA_AUDIO_FILE);
            mPlayer.play(mAudioFile.getAudioFileUrl());
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onPrepared(MediaPlayer player) {
        player.start();
        playbackStartedNotification("SkyEng", mAudioFile.getTitle(), mAudioFile.getImageBitmap());
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }


    @Override
    public void onDestroy() {
        mPlayer.release();
        this.unregisterReceiver(mPlayerBroadcast);
    }

    private Notification playbackStartedNotification(String title, String message, Bitmap largeIcon) {
        Intent intent = new Intent(this, AudioListActivity.class);
        intent.setAction(ACTION_PLAY);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_MESSAGE, message);
        NotificationCompat.Builder builder = initBasicBuilder(title, message, intent);
        if(largeIcon==null){
            largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_play_white);
        }
        builder.setLargeIcon(largeIcon);
        return builder.build();
    }

    private NotificationCompat.Builder initBasicBuilder(String title, String text, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_play_white)
                .setContentTitle(title)
                .setContentText(text);
        if (intent != null) {
            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
            taskStackBuilder.addNextIntentWithParentStack(intent);
        }
        return builder;
    }
}

