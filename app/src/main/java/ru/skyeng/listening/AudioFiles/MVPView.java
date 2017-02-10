package ru.skyeng.listening.AudioFiles;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import ru.skyeng.listening.AudioFiles.domain.AudioFile;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public interface MVPView<T> extends MvpLceView<T> {
    Context getAppContext();
    Context getActivityContext();
}

