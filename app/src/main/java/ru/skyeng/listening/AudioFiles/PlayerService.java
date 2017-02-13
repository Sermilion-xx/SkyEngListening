package ru.skyeng.listening.AudioFiles;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import java.io.IOException;

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

    private static final String WIFI_PLAYER_LOCK = "playerLock";
    public static final String ACTION_PLAY = "ru.skyeng.ACTION_PLAY";
    public static final String ACTION_PAUSE = "ru.skyeng.ACTION_PAUSE";
    public static final String ACTION_CONTINUE = "ru.skyeng.ACTION_CONTINUE";
    public static final String AUDIO_URL = "audioUrl";
    private AudioPlayer mPlayer;
    private WifiManager.WifiLock wifiLock;
    private String mUri;

    private BroadcastReceiver mPlayerBroadcast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_PLAY)) {
                String audioUrl = intent.getStringExtra(AUDIO_URL);
                try {
                    if (mPlayer.isPaused() && mPlayer.getExtraData().equals(audioUrl)) {
                        mPlayer.setPaused(false);
                        mPlayer.getMediaPlayer().prepareAsync();
                    } else {
                        mPlayer.playAudio(audioUrl);
                        mPlayer.setExtraData(audioUrl);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (intent.getAction().equals(ACTION_PAUSE)) {
                pausePlayback();
            }else if(intent.getAction().equals(ACTION_CONTINUE)){
                mPlayer.playPause();
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
    }

    private void initPlayer() {
        if(mPlayer!=null){
            mPlayer.killMediaPlayer();
        }
        mPlayer = new AudioPlayer();
        mPlayer.getMediaPlayer().setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, WIFI_PLAYER_LOCK);
        wifiLock.acquire();
        mPlayer.getMediaPlayer().setOnPreparedListener(this);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(ACTION_PLAY)) {
            mUri = intent.getStringExtra(AUDIO_URL);
            try {
                initPlayer();
                mPlayer.setDataSource(mUri);
                mPlayer.getMediaPlayer().prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    private void pausePlayback() {
        if(wifiLock.isHeld())
            wifiLock.release();
        mPlayer.setPaused(true);
        mPlayer.playPause();
    }

    @Override
    public void onDestroy() {
        if (!mPlayer.isNull()) mPlayer.release();
        this.unregisterReceiver(mPlayerBroadcast);
    }
}

