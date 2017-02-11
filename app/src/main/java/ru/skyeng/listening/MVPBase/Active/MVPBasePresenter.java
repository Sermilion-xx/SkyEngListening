package ru.skyeng.listening.MVPBase.Active;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import ru.skyeng.listening.CommonCoponents.RequestParams;
import ru.skyeng.listening.MVPBase.MVPModel;
import ru.skyeng.listening.MVPBase.MVPPresenter;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public abstract class MVPBasePresenter<L, P, E extends RequestParams, V extends MvpView>
        extends MvpBasePresenter<V> implements MVPPresenter<L, P, E> {

    protected MVPModel<L, P, E> mModel;
    protected boolean isViewFragment;

    public MVPBasePresenter(boolean isViewFragment){
        this.isViewFragment = isViewFragment;
    }

    private MVPBasePresenter(){

    }

    @Override
    public Context getAppContext() {
        if (getView() != null) {
            if (isViewFragment) {
                return ((Fragment) getView()).getActivity().getApplicationContext();
            }else {
                return ((Activity) getView()).getApplicationContext();
            }
        }else {
            return null;
        }
    }

    @Override
    public Context getActivityContext() {
        if (getView() != null) {
            if (isViewFragment) {
                return ((Fragment) getView()).getActivity();
            }else {
                return ((Activity) getView());
            }
        }else {
            return null;
        }
    }

    @Override
    public void setModel(MVPModel<L, P, E> model) {
        mModel = model;
    }

    @Override
    public MVPModel getModel() {
        return mModel;
    }

}
