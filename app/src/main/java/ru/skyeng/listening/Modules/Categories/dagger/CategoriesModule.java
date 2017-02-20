package ru.skyeng.listening.Modules.Categories.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.skyeng.listening.CommonComponents.ServiceGenerator;
import ru.skyeng.listening.Modules.Categories.CategoriesModel;
import ru.skyeng.listening.Modules.Categories.CategoriesPresenter;
import ru.skyeng.listening.Modules.Categories.network.TagsService;

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
class CategoriesModule {
    @Provides
    @Singleton
    CategoriesPresenter getCategoriesPresenter(){
        return new CategoriesPresenter();
    }

    @Provides
    @Singleton
    CategoriesModel getCategoriesModel(){
        return new CategoriesModel();
    }

    @Provides
    @Singleton
    TagsService getTagsService(){
        ServiceGenerator serviceGenerator = new ServiceGenerator();
        return serviceGenerator.createService(TagsService.class);
    }

}
