package ru.skyeng.listening.CommonCoponents;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import ru.skyeng.listening.AudioFiles.AudioListFragment;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class FragmentFactory {

    @Nullable
    public static Fragment createFragmentWithName(Class<? extends Fragment> fragmentClass){
        if(fragmentClass.getName().equals(AudioListFragment.class.getName())){
            return new AudioListFragment();
        }else {
            return null;
        }
    }
}