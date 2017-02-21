package ru.skyeng.listening.MVPBase;

import android.content.Context;

import io.reactivex.Observer;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public interface MVPPresenter<L, P, E> {
    Context getAppContext();
    Context getActivityContext();
    void setModel(MVPModel<L, P, E> models);
    void clear();
    MVPModel<L,P,E> getModel();
    P getData();
    void loadData(boolean pullToRefresh, E params);
}
