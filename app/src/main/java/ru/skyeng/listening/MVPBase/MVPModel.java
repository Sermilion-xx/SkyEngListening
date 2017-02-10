package ru.skyeng.listening.MVPBase;

import android.os.Bundle;

import retrofit2.Callback;
import ru.skyeng.listening.CommonCoponents.RequestParams;
import ru.skyeng.listening.CommonCoponents.SECallback;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public interface MVPModel<L, P, E extends RequestParams> {
    void loadData(SECallback<P> callback, E params);
    P processResult(L data);
    P getItems();
    Bundle getExtraData();
}
