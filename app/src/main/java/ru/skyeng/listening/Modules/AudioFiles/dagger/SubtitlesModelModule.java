package ru.skyeng.listening.Modules.AudioFiles.dagger;

import dagger.Module;
import dagger.Provides;
import ru.skyeng.listening.CommonComponents.ServiceGenerator;
import ru.skyeng.listening.Modules.AudioFiles.network.AudioFilesService;
import ru.skyeng.listening.Modules.AudioFiles.network.SubtitlesService;

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
class SubtitlesModelModule {

    @Provides
    SubtitlesService getSubtitlesService(){
        ServiceGenerator serviceGenerator = new ServiceGenerator();
        return serviceGenerator.createService(SubtitlesService.class);
    }

}
