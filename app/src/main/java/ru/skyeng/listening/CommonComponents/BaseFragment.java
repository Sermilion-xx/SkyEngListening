package ru.skyeng.listening.CommonComponents;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.skyeng.listening.CommonComponents.Interfaces.ActivityExtensions;
import ru.skyeng.listening.CommonComponents.Interfaces.ModelData;
import ru.skyeng.listening.MVPBase.MVPPresenter;
import ru.skyeng.listening.MVPBase.MVPView;
import ru.skyeng.listening.R;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 15/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public abstract class BaseFragment<P,
        Presenter extends MvpPresenter<MVPView<List<P>>>,
        Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>, M extends ModelData<P>>
        extends MvpLceFragment<SwipeRefreshLayout, List<P>, MVPView<List<P>>, Presenter>
        implements MVPView<List<P>>,
        SwipeRefreshLayout.OnRefreshListener, Observer<M> {

    protected boolean isRefreshing;
    public Adapter mAdapter;

    @Override
    public void onRefresh() {
        isRefreshing = true;
        loadData(true);
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        if (e != null)
            return e.getMessage();
        return getActivityContext().getString(R.string.unknown_error);
    }

    @NonNull
    @Override
    public Presenter createPresenter() {
        return presenter;
    }

    @Override
    public void setData(List<P> data) {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        if (!pullToRefresh)
            ((ActivityExtensions) getActivity()).hideProgress();
        ((MVPPresenter)presenter).loadData(pullToRefresh, null);
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        super.showLoading(pullToRefresh);
        contentView.setRefreshing(pullToRefresh);
    }

    @Override
    public Application getAppContext() {
        return (Application) getActivity().getApplicationContext();
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (!isRefreshing)
            ((ActivityExtensions) getActivity()).showProgress();
    }

    @Override
    public void onNext(M value) {
        ((MVPPresenter)presenter).getModel().setData(value);
        isRefreshing = false;
        setData(value.getPrimaryData());
    }

    @Override
    public void onError(Throwable e) {
        isRefreshing = false;
        showError(e.getCause(), isRefreshing);
        contentView.setRefreshing(false);
        e.printStackTrace();
        setData(new ArrayList<>());
    }

    @Override
    public void onComplete() {
        ((ActivityExtensions) getActivity()).hideProgress();
        contentView.setRefreshing(isRefreshing);
    }
}
