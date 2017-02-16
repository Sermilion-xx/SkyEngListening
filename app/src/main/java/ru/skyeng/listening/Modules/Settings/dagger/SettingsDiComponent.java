package ru.skyeng.listening.Modules.Settings.dagger;

import javax.inject.Singleton;

import dagger.Component;
import ru.skyeng.listening.Modules.Settings.SettingsFragment;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */
@Singleton
@Component(modules = {SettingsModule.class})
public interface SettingsDiComponent {
    void inject(SettingsFragment fragment);
}
