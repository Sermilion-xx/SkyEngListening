package ru.skyeng.listening.Modules.AudioFiles.player;

import android.app.Notification;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.NonNull;
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

import javax.inject.Inject;

import ru.skyeng.listening.CommonComponents.SEApplication;
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
    public static final String ACTION_AUDIO_STATE = DOMAIN + ".ACTION_STARTED";
    public static final String ACTION_DID_NOT_STAR = DOMAIN + ".ACTION_PLAYER_DOD_NOT_START";
    public static final String KEY_CURRENT_AUDIO = "PLAYER_STATE";
    public final static int MESSAGE_PLAY = 1;
    public final static int MESSAGE_PAUSE = 2;
    public final static int MESSAGE_CONTINUE = 3;
    public final static int MESSAGE_PLAYBACK_TIME = 4;
    public final static int MESSAGE_PLAYBACK_SEARCH = 5;
    public static final int MESSAGE_START_BUFFERING = 6;
    public static final int MESSAGE_SUBTITLE_TIME = 7;
    public static final String BINDER_MESSENGER = "MESSENGER";
    public static final int MESSAGE_PLAYING_FILE_STATE_FOR_COVER = 8;
    public static final int MESSAGE_GET_PLAYING_FILE_ID = 10;
    public static final int MESSAGE_GET_PLAYING_FILE_DURATION = 11;

    private static final int MESSAGE_SEND_PLAYING_FILE = 9;
    public static final int MESSAGE_UPDATE_PLAYER_UI = 12;
    public static final String AUDIO_ELAPSED_TIME = "AUDIO_ELAPSED_TIME";
    public static final String AUDIO_DURATION = "AUDIO_DURATION";
    public static final int MESSAGE_UPDATE_ADAPTER = 13;
    private int mPlaybackInterval = 1000;
    private int mSubtitleInterval = 100;
    private Handler mPlaybackHandler;
    private AudioPlayer mPlayer;
    Messenger messenger;
    private Messenger outMessenger;

    public AudioPlayer getPlayer() {
        return mPlayer;
    }

    @Inject
    public void setPlayer(@NonNull AudioPlayer player) {
        this.mPlayer = player;
        mPlayer.setContext(this);
        mPlayer.setEventListener(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        ((SEApplication) getApplicationContext()).getPlayerServiceDiComponent().inject(this);
        final IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_PAUSE);
        filter.addAction(ACTION_PLAY);
        filter.addAction(ACTION_CONTINUE);
        mPlaybackHandler = new Handler();
        messenger = new Messenger(new IncomingHandler(this));
    }

    public void startSendingPlaybackTime() {
        mPlaybackSenderRunnable.run();
        mSubtitleRunnable.run();
    }

    public void stopSendingPlaybackTime() {
        mPlaybackHandler.removeCallbacks(mPlaybackSenderRunnable);
        mPlaybackHandler.removeCallbacks(mSubtitleRunnable);
    }

    Runnable mPlaybackSenderRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                Message playbackTime = new Message();
                playbackTime.what = MESSAGE_PLAYBACK_TIME;
                Bundle bundle = new Bundle();
                bundle.putLong(AUDIO_ELAPSED_TIME, getCurrentPlaybackTime());
                bundle.putLong(AUDIO_DURATION, getAudioDuration());
                playbackTime.obj = bundle;
                outMessenger.send(playbackTime);
            } catch (RemoteException e) {
                e.printStackTrace();
            } finally {
                mPlaybackHandler.postDelayed(mPlaybackSenderRunnable, mPlaybackInterval);
            }
        }
    };

    Runnable mSubtitleRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                Message subtitleTime = new Message();
                subtitleTime.what = MESSAGE_SUBTITLE_TIME;
                subtitleTime.obj = getCurrentPlaybackTime();
                outMessenger.send(subtitleTime);
            } catch (RemoteException e) {
                e.printStackTrace();
            } finally {
                mPlaybackHandler.postDelayed(mSubtitleRunnable, mSubtitleInterval);
            }
        }
    };

    public void sendPlayingAudioFile(int messageType) {
        try {
            Message playingFile = new Message();
            playingFile.what = messageType;
            playingFile.obj = mPlayer.getAudioFile();
            outMessenger.send(playingFile);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private long getCurrentPlaybackTime() {
        return mPlayer.getCurrentPosition();
    }

    private long getAudioDuration(){
        return mPlayer.getAudioFile().getDurationInSeconds();
    }

    public void sendAudioDidNotStartBroadcast() {
        Intent intent = new Intent(ACTION_DID_NOT_STAR);
        sendBroadcast(intent);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            outMessenger = (Messenger) extras.get(BINDER_MESSENGER);
        }
        return messenger.getBinder();
    }

    @Override
    public void onDestroy() {
        stopSendingPlaybackTime();
        mPlayer.release();
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        System.out.println();
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        if (isLoading) {
            Intent intent = new Intent(ACTION_AUDIO_STATE);
            mPlayer.getAudioFile().setLoading(true);
            intent.putExtra(KEY_CURRENT_AUDIO, mPlayer.getAudioFile());
            sendBroadcast(intent);
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == 3 && mPlayer.getPlayer().getPlayWhenReady()) {
            mPlayer.getAudioFile().setLoading(false);
            mPlayer.setState(PlayerState.PLAY);
            Intent intent = new Intent(ACTION_AUDIO_STATE);
            intent.putExtra(KEY_CURRENT_AUDIO, mPlayer.getAudioFile());
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

    public void setPlayerSource(Bundle bundle) {
        mPlayer.setPlaySource(bundle.getString(EXTRA_AUDIO_URL));
        Handler timer = new Handler();
        timer.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        if (mPlayer.getState() != PlayerState.PLAY) {
                            mPlayer.pause();
                            sendAudioDidNotStartBroadcast();
                        }
                    }
                }
                , 10000);
    }

    public void sendPlayerState() {
        try {
            Message playerState = new Message();
            playerState.what = MESSAGE_PLAYING_FILE_STATE_FOR_COVER;
            playerState.obj = mPlayer.getState();
            outMessenger.send(playerState);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}

