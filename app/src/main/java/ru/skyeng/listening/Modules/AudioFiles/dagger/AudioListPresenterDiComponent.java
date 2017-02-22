package ru.skyeng.listening.Modules.AudioFiles.dagger;

import javax.inject.Singleton;

import dagger.Component;
import ru.skyeng.listening.Modules.AudioFiles.AudioListActivity;
import ru.skyeng.listening.Modules.AudioFiles.AudioListPresenter;

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
@Component(modules = {AudioListPresenterModule.class})
public interface AudioListPresenterDiComponent {
    void inject(AudioListPresenter presenter);
}
