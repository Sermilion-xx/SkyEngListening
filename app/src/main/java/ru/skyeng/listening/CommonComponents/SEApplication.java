package ru.skyeng.listening.CommonComponents;

import android.app.Application;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;

import ru.skyeng.listening.Modules.AudioFiles.dagger.AudioListDiComponent;
import ru.skyeng.listening.Modules.AudioFiles.dagger.DaggerAudioListDiComponent;
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

    private static SEApplication INSTANCE;

    public static SEApplication getINSTANCE() {
        return INSTANCE;
    }

    private AudioListDiComponent audioListDiComponent;
    private CategoriesDiComponent categoriesDiComponent;
    private CategoriesPresenterDiComponent categoriesPresenterDiComponent;
    private CategoriesModelDiComponent categoriesModelDiComponent;
    protected String userAgent;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        audioListDiComponent = DaggerAudioListDiComponent.builder().build();
        categoriesDiComponent = DaggerCategoriesDiComponent.builder().build();
        categoriesPresenterDiComponent = DaggerCategoriesPresenterDiComponent.builder().build();
        categoriesModelDiComponent = DaggerCategoriesModelDiComponent.builder().build();
        userAgent = Util.getUserAgent(this, getString(R.string.skyeng_listening));
    }

    public AudioListDiComponent getAudioListDiComponent() {
        return audioListDiComponent;
    }

    public CategoriesDiComponent getCategoriesDiComponent() {
        return categoriesDiComponent;
    }

    public CategoriesPresenterDiComponent getCategoriesPresenterDiComponent() {
        return categoriesPresenterDiComponent;
    }

    public CategoriesModelDiComponent getCategoriesModelDiComponent() {
        return categoriesModelDiComponent;
    }

    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(this, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
    }

}
