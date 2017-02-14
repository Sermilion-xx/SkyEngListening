package ru.skyeng.listening.AudioFiles;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;

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

public class AudioPlayer {

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private EventLogger eventLogger;
    private Handler mainHandler;
    private Context mContext;
    private SimpleExoPlayer mPlayer;
    private DataSource.Factory dataSourceFactory;
    private String extraData;
    private boolean paused;
    private int state;

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public AudioPlayer(Context context){
        mainHandler = new Handler();
        mContext = context;
        state = 0;
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "SkyEng Listening"), bandwidthMeter);
        mPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
        eventLogger = new EventLogger(trackSelector);
        mPlayer.addListener(eventLogger);
        mPlayer.setAudioDebugListener(eventLogger);
        mPlayer.setVideoDebugListener(eventLogger);
        mPlayer.setMetadataOutput(eventLogger);
    }

    public void play(String url){
        extraData = url;
        state = 1;
        MediaSource mediaSource = buildMediaSource(Uri.parse(url), "");
        mPlayer.prepare(mediaSource);
        mPlayer.setPlayWhenReady(true);
    }

    public boolean isPaused() {
        return state == 2;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void pause() {
        state = 2;
        mPlayer.setPlayWhenReady(false);
    }

    public void play() {
        state = 1;
        mPlayer.setPlayWhenReady(true);
    }

    public void release(){
        mPlayer.release();
    }

    private MediaSource buildMediaSource(Uri uri, String overrideExtension) {
        int type = Util.inferContentType(!TextUtils.isEmpty(overrideExtension) ? "." + overrideExtension
                : uri.getLastPathSegment());
        switch (type) {
            case C.TYPE_SS:
                return new SsMediaSource(uri, buildDataSourceFactory(true),
                        new DefaultSsChunkSource.Factory(dataSourceFactory), mainHandler, eventLogger);
            case C.TYPE_DASH:
                return new DashMediaSource(uri, buildDataSourceFactory(true),
                        new DefaultDashChunkSource.Factory(dataSourceFactory), mainHandler, eventLogger);
            case C.TYPE_HLS:
                return new HlsMediaSource(uri, dataSourceFactory, mainHandler, eventLogger);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource(uri, dataSourceFactory, new DefaultExtractorsFactory(),
                        mainHandler, eventLogger);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }

    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return ((SEApplication) mContext.getApplicationContext())
                .buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }
}
