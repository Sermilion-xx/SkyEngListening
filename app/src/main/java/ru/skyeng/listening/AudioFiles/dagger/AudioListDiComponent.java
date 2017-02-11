package ru.skyeng.listening.AudioFiles.dagger;

import javax.inject.Singleton;

import dagger.Component;
import ru.skyeng.listening.AudioFiles.AudioListFragment;

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
@Component(modules = {AudioListModule.class})
public interface AudioListDiComponent {
    void inject(AudioListFragment fragment);
}
