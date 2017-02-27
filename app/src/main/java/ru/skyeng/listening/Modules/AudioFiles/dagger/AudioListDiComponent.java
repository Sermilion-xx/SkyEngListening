package ru.skyeng.listening.Modules.AudioFiles.dagger;

import javax.inject.Singleton;

import dagger.Component;
import ru.skyeng.listening.Modules.AudioFiles.AudioListActivity;
import ru.skyeng.listening.Modules.AudioFiles.AudioListModel;
import ru.skyeng.listening.Modules.AudioFiles.AudioListPresenter;
import ru.skyeng.listening.Modules.AudioFiles.SubtitlesModel;
import ru.skyeng.listening.Modules.AudioFiles.player.PlayerService;

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
@Component(modules = {AudioListModule.class,
        AudioListModelModule.class,
        AudioListPresenterModule.class,
        SubtitlesModelModule.class})
public interface AudioListDiComponent {
    void inject(AudioListActivity fragment);
    void inject(AudioListModel model);
    void inject(AudioListPresenter fragment);
    void inject(SubtitlesModel model);
}
