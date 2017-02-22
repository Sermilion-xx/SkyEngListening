package ru.skyeng.listening.Modules.AudioFiles.player;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

import ru.skyeng.listening.CommonComponents.SEApplication;
import ru.skyeng.listening.Modules.AudioFiles.model.AudioFile;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 13/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

class AudioPlayer {

    private static final int BUFFER_SEGMENT_SIZE = 1024;
    private static final int MAIN_BUFFER_SEGMENTS = 254;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private Context mContext;
    private SimpleExoPlayer mPlayer;
    private String extraData;
    private PlayerState state;
    private ExoPlayer.EventListener mEventListener;
    private ExtractorsFactory extractorsFactory;
    private boolean shouldAutoPlay;
    private DataSource.Factory mediaDataSourceFactory;
    private AudioFile mAudioFile;

    AudioPlayer(Context context, ExoPlayer.EventListener eventListener) {
        shouldAutoPlay = true;
        mEventListener = eventListener;
        mContext = context;
        setState(PlayerState.STOP);
        mediaDataSourceFactory = buildDataSourceFactory(true);
        extractorsFactory = new DefaultExtractorsFactory();
        initializePlayer();
    }

    public void setAudioFile(AudioFile mAudioFile) {
        this.mAudioFile = mAudioFile;
    }

    public AudioFile getAudioFile() {
        return mAudioFile;
    }

    public SimpleExoPlayer getPlayer() {
        return mPlayer;
    }

    void setPlaySource(String url) {
        extraData = url;
        setState(PlayerState.STOP);
        setMediaSource(url);
    }

    private void setMediaSource(String audioUrl) {
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(audioUrl),
                mediaDataSourceFactory, extractorsFactory, null, null);
        mPlayer.prepare(mediaSource, true, true);
    }

    public long getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    boolean isPaused() {
        return state == PlayerState.PAUSE;
    }

    void setState(PlayerState state) {
        this.state = state;
        if (mAudioFile != null) {
            mAudioFile.setState(state);
        }
    }

    public PlayerState getState() {
        return state;
    }

    void pause() {
        mAudioFile.setLoading(false);
        setState(PlayerState.PAUSE);
        mPlayer.setPlayWhenReady(false);
    }

    void play() {
        setState(PlayerState.PLAY);
        mPlayer.setPlayWhenReady(true);
    }

    void release() {
        mPlayer.release();
    }

    private void initializePlayer() {
        if (mPlayer == null) {
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveVideoTrackSelection.Factory(BANDWIDTH_METER);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();
            loadControl.shouldStartPlayback(0, false);
            mPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl,
                    null);
            mPlayer.addListener(mEventListener);
        }
    }

    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return ((SEApplication) mContext.getApplicationContext())
                .buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    public long getDuration() {
        return mPlayer.getDuration();
    }
}
