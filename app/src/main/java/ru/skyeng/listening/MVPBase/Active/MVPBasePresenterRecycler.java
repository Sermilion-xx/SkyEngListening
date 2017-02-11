package ru.skyeng.listening.MVPBase.Active;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.Collection;
import java.util.List;

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

public abstract class MVPBasePresenterRecycler<L, P extends List<T>, E extends RequestParams, V extends MvpView, T>
        extends MVPBasePresenter<L, P, E, V>
        implements MVPPresenterRecycler<L, P, E, T>{

    public MVPBasePresenterRecycler(boolean isViewFragment) {
        super(isViewFragment);
    }


    @Override
    public T getItem(int position) {
        return mModel.getItems().get(position);
    }

    @Override
    public List<T> getItems() {
        return mModel.getItems();
    }

    @Override
    public int getItemCount() {
        return mModel.getItems().size();
    }

    @Override
    public void addItem(T item) {
        mModel.getItems().add(item);
    }

    @Override
    public void addAll(Collection<? extends T> items) {
        mModel.getItems().addAll(items);
    }

    @Override
    public void remove(int index) {
        mModel.getItems().remove(index);
    }

    @Override
    public void remove(T item) {
        mModel.getItems().remove(item);
    }

    @Override
    public void removeAll(Collection<? extends T> items) {
        mModel.getItems().removeAll(items);
    }

    @Override
    public void clear() {
        mModel.getItems().clear();
    }
}
