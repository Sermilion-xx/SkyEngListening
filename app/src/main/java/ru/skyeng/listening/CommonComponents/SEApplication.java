package ru.skyeng.listening.CommonComponents;

import android.app.Application;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;

import ru.skyeng.listening.Modules.AudioFiles.dagger.AudioListDiComponent;
import ru.skyeng.listening.Modules.AudioFiles.dagger.AudioListModelDiComponent;
import ru.skyeng.listening.Modules.AudioFiles.dagger.AudioListPresenterDiComponent;
import ru.skyeng.listening.Modules.AudioFiles.dagger.DaggerAudioListDiComponent;
import ru.skyeng.listening.Modules.AudioFiles.dagger.DaggerAudioListModelDiComponent;
import ru.skyeng.listening.Modules.AudioFiles.dagger.DaggerAudioListPresenterDiComponent;
import ru.skyeng.listening.Modules.AudioFiles.dagger.DaggerPlayerServiceDiComponent;
import ru.skyeng.listening.Modules.AudioFiles.dagger.DaggerSubtitlesModelDiComponent;
import ru.skyeng.listening.Modules.AudioFiles.dagger.PlayerServiceDiComponent;
import ru.skyeng.listening.Modules.AudioFiles.dagger.SubtitlesModelDiComponent;
import ru.skyeng.listening.Modules.Categories.dagger.CategoriesDiComponent;
import ru.skyeng.listening.Modules.Categories.dagger.CategoriesModelDiComponent;
import ru.skyeng.listening.Modules.Categories.dagger.CategoriesPresenterDiComponent;
import ru.skyeng.listening.Modules.Categories.dagger.DaggerCategoriesDiComponent;
import ru.skyeng.listening.Modules.Categories.dagger.DaggerCategoriesModelDiComponent;
import ru.skyeng.listening.Modules.Categories.dagger.DaggerCategoriesPresenterDiComponent;
import ru.skyeng.listening.R;

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
    private AudioListPresenterDiComponent audioListPresenterDiComponent;
    private CategoriesDiComponent categoriesDiComponent;
    private CategoriesPresenterDiComponent categoriesPresenterDiComponent;
    private CategoriesModelDiComponent categoriesModelDiComponent;
    protected String userAgent;
    private AudioListModelDiComponent audioListModelDiComponent;
    private SubtitlesModelDiComponent subtitlesModelListDiComponent;
    private PlayerServiceDiComponent playerServiceDiComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        audioListDiComponent = DaggerAudioListDiComponent.builder().build();
        categoriesDiComponent = DaggerCategoriesDiComponent.builder().build();
        audioListPresenterDiComponent = DaggerAudioListPresenterDiComponent.builder().build();
        categoriesPresenterDiComponent = DaggerCategoriesPresenterDiComponent.builder().build();
        categoriesModelDiComponent = DaggerCategoriesModelDiComponent.builder().build();
        audioListModelDiComponent = DaggerAudioListModelDiComponent.builder().build();
        subtitlesModelListDiComponent = DaggerSubtitlesModelDiComponent.builder().build();
        playerServiceDiComponent = DaggerPlayerServiceDiComponent.builder().build();
        userAgent = Util.getUserAgent(this, getString(R.string.skyeng_listening));
    }

    public AudioListDiComponent getAudioListDiComponent() {
        return audioListDiComponent;
    }

    public CategoriesDiComponent getCategoriesDiComponent() {
        return categoriesDiComponent;
    }

    public AudioListPresenterDiComponent getAudioListPresenterDiComponent() {
        return audioListPresenterDiComponent;
    }

    public CategoriesPresenterDiComponent getCategoriesPresenterDiComponent() {
        return categoriesPresenterDiComponent;
    }

    public CategoriesModelDiComponent getCategoriesModelDiComponent() {
        return categoriesModelDiComponent;
    }

    public AudioListModelDiComponent getAudioListModelDiComponent() {
        return audioListModelDiComponent;
    }

    public SubtitlesModelDiComponent getSubtitlesModelListDiComponent() {
        return subtitlesModelListDiComponent;
    }

    public PlayerServiceDiComponent getPlayerServiceDiComponent() {
        return playerServiceDiComponent;
    }

    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(this, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
    }

}
