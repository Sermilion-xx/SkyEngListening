package ru.skyeng.listening.CommonComponents;

import android.app.Application;

import com.google.android.exoplayer2.BuildConfig;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;

import ru.skyeng.listening.Modules.AudioFiles.dagger.AudioListDiComponent;
import ru.skyeng.listening.Modules.AudioFiles.dagger.DaggerAudioListDiComponent;
import ru.skyeng.listening.Modules.Categories.dagger.CategoriesDiComponent;
import ru.skyeng.listening.Modules.Categories.dagger.DaggerCategoriesDiComponent;

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
    private CategoriesDiComponent categoriesDiComponent;
    protected String userAgent;

    @Override
    public void onCreate() {
        super.onCreate();
        audioListDiComponent = DaggerAudioListDiComponent.builder().build();
        categoriesDiComponent = DaggerCategoriesDiComponent.builder().build();
        userAgent = Util.getUserAgent(this, "SkyEng Listening");
    }

    public AudioListDiComponent getAudioListDiComponent() {
        return audioListDiComponent;
    }

    public CategoriesDiComponent getCategoriesDiComponent() {
        return categoriesDiComponent;
    }

    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(this, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
    }

}
