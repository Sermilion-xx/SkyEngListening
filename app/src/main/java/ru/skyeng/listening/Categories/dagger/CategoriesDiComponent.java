package ru.skyeng.listening.Categories.dagger;

import javax.inject.Singleton;

import dagger.Component;
import ru.skyeng.listening.Categories.CategoriesFragment;

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
@Component(modules = {CategoriesModule.class})
public interface CategoriesDiComponent {
    void inject(CategoriesFragment fragment);
}
