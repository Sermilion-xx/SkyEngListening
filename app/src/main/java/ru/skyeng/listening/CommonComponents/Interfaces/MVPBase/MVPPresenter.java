package ru.skyeng.listening.CommonComponents.Interfaces.MVPBase;

import android.content.Context;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public interface MVPPresenter<M, P, E> {
    Context getAppContext();
    Context getActivityContext();
    void setModel(M models);
    void clear();
    M getModel();
    P getData();
    void loadData(boolean pullToRefresh);
}
