package ru.skyeng.listening.CommonComponents;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import ru.skyeng.listening.Modules.Categories.CategoriesFragment;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class FragmentFactory {

    @Nullable
    public static Fragment createFragmentWithName(Class<? extends Fragment> fragmentClass){
        if(fragmentClass.getName().equals(CategoriesFragment.class.getName())){
            return new CategoriesFragment();
        }else {
            return null;
        }
    }
}
