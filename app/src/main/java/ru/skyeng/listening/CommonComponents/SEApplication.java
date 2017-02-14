package ru.skyeng.listening.CommonComponents;

import android.app.Application;

import com.google.android.exoplayer2.BuildConfig;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;

import ru.skyeng.listening.AudioFiles.dagger.AudioListDiComponent;
import ru.skyeng.listening.AudioFiles.dagger.DaggerAudioListDiComponent;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 11/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class SEApplication extends Application {

    private AudioListDiComponent audioListDiComponent;
    protected String userAgent;

    @Override
    public void onCreate() {
        super.onCreate();
        audioListDiComponent = DaggerAudioListDiComponent.builder().build();
        userAgent = Util.getUserAgent(this, "SkyEng Listening");
    }

    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(this, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
    }

    public AudioListDiComponent getAudioListDiComponent() {
        return audioListDiComponent;
    }

    public boolean useExtensionRenderers() {
        return BuildConfig.FLAVOR.equals("withExtensions");
    }

}
