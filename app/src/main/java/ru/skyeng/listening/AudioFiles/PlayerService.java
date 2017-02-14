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
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSpec;

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

public class PlayerService extends Service implements ExoPlayer.EventListener,
        AdaptiveMediaSourceEventListener, ExtractorMediaSource.EventListener {

    public static final String DOMAIN = "ru.skyeng.listening";
    public static final String ACTION_PLAY = DOMAIN+".ACTION_PLAY";
    public static final String ACTION_PAUSE = DOMAIN+".ACTION_PAUSE";
    public static final String ACTION_CONTINUE = DOMAIN+".ACTION_CONTINUE";
    public static final String AUDIO_URL = "audioUrl";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_AUDIO_URL = "audioUrl";
    public static final String ACTION_AUDIO_STATE = DOMAIN+".audioStarted";
    public static final String KEY_PLAYER_STATE = "PLAYER_STATE";
    private AudioPlayer mPlayer;

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
        mPlayer = new AudioPlayer(this, this);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(ACTION_PLAY)) {
            String url = intent.getStringExtra(EXTRA_AUDIO_URL);
            mPlayer.play(url);
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        System.out.println();
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        System.out.println();
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        Intent intent = new Intent(ACTION_AUDIO_STATE);
        intent.putExtra(KEY_PLAYER_STATE, isLoading);
        sendBroadcast(intent);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        System.out.println();
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        System.out.println();
    }

    @Override
    public void onPositionDiscontinuity() {
        System.out.println();
    }

    @Override
    public void onLoadStarted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs) {
        System.out.println();
    }

    @Override
    public void onLoadCompleted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {
        System.out.println();
    }

    @Override
    public void onLoadCanceled(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {
        System.out.println();
    }

    @Override
    public void onLoadError(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded, IOException error, boolean wasCanceled) {
        System.out.println();
    }

    @Override
    public void onUpstreamDiscarded(int trackType, long mediaStartTimeMs, long mediaEndTimeMs) {

    }

    @Override
    public void onDownstreamFormatChanged(int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaTimeMs) {

    }

    @Override
    public void onLoadError(IOException error) {
        System.out.println();
    }



}

