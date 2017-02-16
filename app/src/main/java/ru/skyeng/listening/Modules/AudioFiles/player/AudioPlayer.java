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
    private int state;
    private ExoPlayer.EventListener mEventListener;
    private ExtractorsFactory extractorsFactory;
    private boolean shouldAutoPlay;
    private DataSource.Factory mediaDataSourceFactory;

    public SimpleExoPlayer getPlayer() {
        return mPlayer;
    }

    AudioPlayer(Context context, ExoPlayer.EventListener eventListener){
        shouldAutoPlay = true;
        mEventListener = eventListener;
        mContext = context;
        state = 0;
        mediaDataSourceFactory = buildDataSourceFactory(true);
        extractorsFactory = new DefaultExtractorsFactory();
        initializePlayer();
    }

    void setPlaySource(String url){
        extraData = url;
        state = 0;
        setMediaSource(url);
    }

    private void setMediaSource(String audioUrl) {
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(audioUrl),
                mediaDataSourceFactory, extractorsFactory, null, null);
        mPlayer.prepare(mediaSource, true, true);
    }

    boolean isPaused() {
        return state == 2;
    }

    void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    void pause() {
        state = 2;
        mPlayer.setPlayWhenReady(false);
    }

    void play() {
        state = 1;
        mPlayer.setPlayWhenReady(true);
    }

    void release(){
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
            EventLogger eventLogger = new EventLogger(trackSelector);
            mPlayer.addListener(eventLogger);
            mPlayer.setAudioDebugListener(eventLogger);
            mPlayer.setMetadataOutput(eventLogger);
            mPlayer.setPlayWhenReady(shouldAutoPlay);
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
