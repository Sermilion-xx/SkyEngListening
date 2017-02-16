package ru.skyeng.listening.Modules.Settings.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.skyeng.listening.Modules.Settings.SettingsModel;
import ru.skyeng.listening.Modules.Settings.SettingsPresenter;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

@Module
class SettingsModule {
    @Provides
    @Singleton
    SettingsPresenter getSettingsPresenter(){
        return new SettingsPresenter();
    }

    @Provides
    @Singleton
    SettingsModel getSettingsModel(){
        return new SettingsModel();
    }


}
