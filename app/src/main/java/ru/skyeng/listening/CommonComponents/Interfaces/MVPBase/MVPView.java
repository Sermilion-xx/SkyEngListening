package ru.skyeng.listening.CommonComponents.Interfaces.MVPBase;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public interface MVPView extends MvpView {
    Context getAppContext();
    Context getActivityContext();
}

