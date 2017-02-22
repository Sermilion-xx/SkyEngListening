package ru.skyeng.listening.Modules.Categories.dagger;

import javax.inject.Singleton;

import dagger.Component;
import ru.skyeng.listening.Modules.Categories.CategoriesActivity;
import ru.skyeng.listening.Modules.Categories.CategoriesModel;

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
@Component(modules = {CategoriesModelModule.class})
public interface CategoriesModelDiComponent {
    void inject(CategoriesModel model);
}
