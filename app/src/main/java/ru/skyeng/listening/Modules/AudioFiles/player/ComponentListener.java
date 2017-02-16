package ru.skyeng.listening.Modules.AudioFiles.player;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.SeekBar;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlaybackControlView;

import java.util.Formatter;
import java.util.Locale;

import static com.google.android.exoplayer2.ui.PlaybackControlView.DEFAULT_SEEK_DISPATCHER;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 15/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class ComponentListener implements ExoPlayer.EventListener,
        SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private Context mContext;
    private final StringBuilder formatBuilder;
    private final Formatter formatter;
    private PlaybackControlView.SeekDispatcher seekDispatcher;
    private boolean dragging;
    private Intent updatePlayerIntent;

    private SimpleExoPlayer mPlayer;

    ComponentListener(Context context, SimpleExoPlayer player) {
        mContext = context;
        mPlayer = player;
        formatBuilder = new StringBuilder();
        formatter = new Formatter(formatBuilder, Locale.getDefault());
        seekDispatcher = DEFAULT_SEEK_DISPATCHER;
    }

    private void  updatePlayerView(String category, String value) {
        updatePlayerIntent = new Intent(PlayerService.ACTION_UPDATE_PLAYER);
        updatePlayerIntent.addCategory(category);
        updatePlayerIntent.putExtra(PlayerService.PLAYER_UPDATE_VALUE_1, value);
        mContext.sendBroadcast(updatePlayerIntent);
    }

    private void addSecondExtraToIntent(int value) {
        if (updatePlayerIntent == null) {
            throw new IllegalStateException("Этим методом можно добавлять только второй аргумент");
        }
        updatePlayerIntent.putExtra(PlayerService.PLAYER_UPDATE_VALUE_2, value);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        dragging = true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        long position = positionValue(progress);
        updatePlayerView(PlayerService.CATEGORY_AUDIO_PLAYED, stringForTime(position));
        if (mPlayer != null && !dragging) {
            seekTo(position);
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        dragging = false;
        if (mPlayer != null) {
            seekTo(positionValue(seekBar.getProgress()));
        }
        dragging = false;
        if (mPlayer != null) {
            seekTo(positionValue(seekBar.getProgress()));
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        updateProgress();
    }

    @Override
    public void onPositionDiscontinuity() {
        updateProgress();
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        updateProgress();
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray tracks, TrackSelectionArray selections) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onClick(View view) {

    }

    private long positionValue(int progress) {
        long duration = mPlayer == null ? C.TIME_UNSET : mPlayer.getDuration();
        return duration == C.TIME_UNSET ? 0 : ((duration * progress) / PlayerService.PROGRESS_BAR_MAX);
    }

    private String stringForTime(long timeMs) {
        if (timeMs == C.TIME_UNSET) {
            timeMs = 0;
        }
        long totalSeconds = (timeMs + 500) / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        formatBuilder.setLength(0);
        return hours > 0 ? formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
                : formatter.format("%02d:%02d", minutes, seconds).toString();
    }

    private void seekTo(long positionMs) {
        seekTo(mPlayer.getCurrentWindowIndex(), positionMs);
    }

    private void seekTo(int windowIndex, long positionMs) {
        boolean dispatched = seekDispatcher.dispatchSeek(mPlayer, windowIndex, positionMs);
        if (!dispatched) {
            // The seek wasn't dispatched. If the progress bar was dragged by the user to perform the
            // seek then it'll now be in the wrong position. Trigger a progress update to snap it back.
            updateProgress();
        }
    }

    private void updateProgress() {
        long duration = mPlayer == null ? 0 : mPlayer.getDuration();
        long position = mPlayer == null ? 0 : mPlayer.getCurrentPosition();
        updatePlayerView(PlayerService.CATEGORY_AUDIO_LEFT, stringForTime(duration));
        if (!dragging) {
            updatePlayerView(PlayerService.CATEGORY_AUDIO_SEEK, String.valueOf(progressBarValue(position)));
        }
        long bufferedPosition = mPlayer == null ? 0 : mPlayer.getBufferedPosition();
        addSecondExtraToIntent(progressBarValue(bufferedPosition));
//        mPlayerView.removeCallbacks(updateProgressAction);
        int playbackState = mPlayer == null ? ExoPlayer.STATE_IDLE : mPlayer.getPlaybackState();
        if (playbackState != ExoPlayer.STATE_IDLE && playbackState != ExoPlayer.STATE_ENDED) {
            long delayMs;
            if (mPlayer.getPlayWhenReady() && playbackState == ExoPlayer.STATE_READY) {
                delayMs = 1000 - (position % 1000);
                if (delayMs < 200) {
                    delayMs += 1000;
                }
            } else {
                delayMs = 1000;
            }
//            mPlayerView.postDelayed(updateProgressAction, delayMs);
        }
    }


    private int progressBarValue(long position) {
        long duration = mPlayer == null ? C.TIME_UNSET : mPlayer.getDuration();
        return duration == C.TIME_UNSET || duration == 0 ? 0
                : (int) ((position * PlayerService.PROGRESS_BAR_MAX) / duration);
    }

    private final Runnable updateProgressAction = this::updateProgress;
}