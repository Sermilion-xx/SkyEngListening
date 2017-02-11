package ru.skyeng.listening.MVPBase.Active;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collection;
import java.util.List;

import ru.skyeng.listening.CommonCoponents.RequestParams;
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

public interface MVPPresenterRecycler<L, P, E extends RequestParams, T> extends MVPPresenter<L, P, E> {

    RecyclerView.ViewHolder createViewHolder(ViewGroup parent, int viewType);

    void bindViewHolder(RecyclerView.ViewHolder holder, int position);

    T getItem(int position);

    List<T> getItems();

    int getItemCount();

    void addItem(T item);

    void addAll(Collection<? extends T> items);

    void remove(int index);

    void remove(T item);

    void removeAll(Collection<? extends T> items);

    void clear();
}
