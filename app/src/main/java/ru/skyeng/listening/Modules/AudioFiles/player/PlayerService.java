package ru.skyeng.listening.Modules.AudioFiles.player;

import android.app.Notification;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.exoplayer2.C;
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

import ru.skyeng.listening.Modules.AudioFiles.AudioListActivity;
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
    public static final String ACTION_PLAY = DOMAIN + ".ACTION_PLAY";
    public static final String ACTION_PAUSE = DOMAIN + ".ACTION_PAUSE";
    public static final String ACTION_CONTINUE = DOMAIN + ".ACTION_CONTINUE";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_AUDIO_URL = "audioUrl";
    public static final String EXTRA_TIMELINE_REQUEST = "getTimeline";
    public static final String EXTRA_PLAYBACK_TIMELINE = "playbackTimeLine";
    public static final String ACTION_AUDIO_STATE = DOMAIN + ".ACTION_STARTED";
    public static final String ACTION_UPDATE_PLAYER = DOMAIN + ".ACTION_UPDATE_PLAYER";
    public static final String KEY_PLAYER_STATE = "PLAYER_STATE";
    public static final String PLAYER_UPDATE_VALUE_1 = "updateValue1";
    public static final String PLAYER_UPDATE_VALUE_2 = "updateValue2";
    public static final String CATEGORY_AUDIO_LEFT = DOMAIN + "CATEGORY_AUDIO_LEFT";
    public static final String CATEGORY_AUDIO_SEEK = DOMAIN + "CATEGORY_AUDIO_SEEK";
    public static final String CATEGORY_AUDIO_PLAYED = DOMAIN + "CATEGORY_AUDIO_PLAYED";
    public static final int PROGRESS_BAR_MAX = 1000;

    private static AudioPlayer mPlayer;

//    private final IBinder mBinder = new PlayerBinder();

    static Messenger replyMessanger;
    public final static int MESSAGE_STOP = 0;
    public final static int MESSAGE_PLAY = 1;
    public final static int MESSAGE_PAUSE = 2;
    public final static int MESSAGE_CONTINUE = 3;
    public final static int MESSAGE_PLAYBACK_TIME = 4;

    static class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_PLAY) {
                Bundle bundle = msg.getData();
                mPlayer.setPlaySource(bundle.getString(EXTRA_AUDIO_URL));
                mPlayer.play();
            } else if (msg.what == MESSAGE_PAUSE) {
                mPlayer.pause();
            } else if (msg.what == MESSAGE_CONTINUE) {
                mPlayer.play();
            } else if (msg.what == MESSAGE_PLAYBACK_TIME) {
                replyMessanger = msg.replyTo;
                replyMessanger = msg.replyTo; //init reply messenger
            }
        }
    }

    private static void sendPlaybackTime(Timeline timeline) {
        if (replyMessanger != null && !mPlayer.isPaused())
            try {
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_PLAYBACK_TIMELINE, (Parcelable) timeline);
                message.obj = timeline;
                replyMessanger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
    }

    Messenger messenger = new Messenger(new IncomingHandler());

    private BroadcastReceiver mPlayerBroadcast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_PAUSE)) {
                mPlayer.pause();
            } else if (intent.getAction().equals(ACTION_CONTINUE)) {
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
//        mComponentListener = new ComponentListener(this, mPlayer.getPlayer());
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
//        Bundle bundle = intent.getExtras();
//        mPlayer.setPlaySource(bundle.getString(EXTRA_AUDIO_URL));
//        mPlayer.play();
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

//    public class PlayerBinder extends Binder {
//        public PlayerService getService() {
//            return PlayerService.this;
//        }
//    }

    @Override
    public void onDestroy() {
        mPlayer.release();
        this.unregisterReceiver(mPlayerBroadcast);
    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
//        sendPlaybackTime(timeline);
        Log.d("Timeline", timeline.toString());

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        System.out.println();
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        if (isLoading) {
            Intent intent = new Intent(ACTION_AUDIO_STATE);
            intent.putExtra(KEY_PLAYER_STATE, true);
            sendBroadcast(intent);
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == 3) {
            Intent intent = new Intent(ACTION_AUDIO_STATE);
            intent.putExtra(KEY_PLAYER_STATE, false);
            sendBroadcast(intent);
        }
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


    private int progressBarValue(long position) {
        long duration = mPlayer == null ? C.TIME_UNSET : mPlayer.getDuration();
        return duration == C.TIME_UNSET || duration == 0 ? 0
                : (int) ((position * PROGRESS_BAR_MAX) / duration);
    }

    private Notification playbackStartedNotification(String title, String message, Bitmap largeIcon) {
        Intent intent = new Intent(this, AudioListActivity.class);
        intent.setAction(ACTION_PLAY);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_MESSAGE, message);
        NotificationCompat.Builder builder = initBasicBuilder(title, message, intent);
        if (largeIcon == null) {
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

