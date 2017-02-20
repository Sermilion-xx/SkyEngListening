package ru.skyeng.listening.MVPBase;

import android.os.Bundle;

import io.reactivex.Observer;
import ru.skyeng.listening.CommonComponents.Interfaces.RequestParams;
import ru.skyeng.listening.Utility.asynctask.Consumer;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public interface MVPModel<L, P, E extends RequestParams> {
    void loadData(Observer<L> observable, E params);
    void setData(L data);
    void addData(L data);
    P processResult(L data);
    P getItems();
    Bundle getExtraData();
}
