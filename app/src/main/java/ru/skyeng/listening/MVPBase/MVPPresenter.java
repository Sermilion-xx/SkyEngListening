package ru.skyeng.listening.MVPBase;

import android.content.Context;

import ru.skyeng.listening.CommonCoponents.RequestParams;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public interface MVPPresenter<T,E,V extends RequestParams> {
    Context getAppContext();
    Context getActivityContext();
    void setModel(MVPModel<T,E,V> models);
    MVPModel getModel();
    void loadData(boolean pullToRefresh, RequestParams params);
}
