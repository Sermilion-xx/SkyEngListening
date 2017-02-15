package ru.skyeng.listening.Categories.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.skyeng.listening.Categories.CategoriesAdapter;
import ru.skyeng.listening.Categories.CategoriesModel;
import ru.skyeng.listening.Categories.CategoriesPresenter;

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
    CategoriesAdapter getCategoriesAdapter(){
        return new CategoriesAdapter();
    }

}
