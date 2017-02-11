package ru.skyeng.listening.CommonCoponents;

import android.app.Application;

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

    @Override
    public void onCreate() {
        super.onCreate();
        audioListDiComponent = DaggerAudioListDiComponent.builder().build();
    }

    public AudioListDiComponent getAudioListDiComponent() {
        return audioListDiComponent;
    }
}
