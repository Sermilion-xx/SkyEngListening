package ru.skyeng.listening.Modules.AudioFiles;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.skyeng.listening.Modules.AudioFiles.model.AudioData;
import ru.skyeng.listening.Modules.AudioFiles.model.AudioFile;
import ru.skyeng.listening.Modules.AudioFiles.model.AudioFilesRequestParams;
import ru.skyeng.listening.CommonComponents.SEApplication;
import ru.skyeng.listening.MVPBase.MVPView;
import ru.skyeng.listening.R;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class AudioListFragment extends MvpLceFragment<
        SwipeRefreshLayout,
        List<AudioFile>,
        MVPView<List<AudioFile>>,
        AudioListPresenter>
        implements MVPView<List<AudioFile>>,
        SwipeRefreshLayout.OnRefreshListener, Observer<AudioData> {

    @Override
    @Inject
    public void setPresenter(@NonNull AudioListPresenter presenter) {
        presenter.setObserver(this);
        super.setPresenter(presenter);
    }

    @NonNull
    @Override
    public AudioListPresenter getPresenter() {
        return super.getPresenter();
    }

    @Inject
    void setModel(AudioListModel model) {
        presenter.setModel(model);
    }
    @Inject AudioListAdapter mAdapter;
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.loadingView) ProgressBar mProgress;
    private boolean isRefreshing;
    private AudioFilesRequestParams mRequestParams;

    public void setRequestParams(AudioFilesRequestParams mRequestParams) {
        this.mRequestParams = mRequestParams;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ((SEApplication) getAppContext()).getAudioListDiComponent().inject(this);
        mRequestParams = new AudioFilesRequestParams();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_audio_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);
        ButterKnife.bind(this, view);
        contentView.setOnRefreshListener(this);
        mAdapter.setPresenter(presenter);
        mAdapter.setFragment(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(this.mRecyclerView.getContext(), layoutManager.getOrientation());
        this.mRecyclerView.addItemDecoration(mDividerItemDecoration);
        if ((presenter.getModel()).getItems() == null) {
            loadData(false);
        }
    }

    public void startPlaying(AudioFile item) {
        ((AudioListActivity) getActivity()).startPlayerMessage(item);
    }

    public void continuePlaying(){
        ((AudioListActivity) getActivity()).continuePlayingMessage();
    }

    public void pausePlayer() {
        ((AudioListActivity) getActivity()).pausePlayerMessage();
    }

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
    public AudioListPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void setData(List<AudioFile> data) {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadData(pullToRefresh, mRequestParams);
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        super.showLoading(pullToRefresh);
        contentView.setRefreshing(pullToRefresh);
    }

    @Override
    public Context getAppContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    @Override
    public void onSubscribe(Disposable d) {
        ((AudioListActivity)getActivityContext()).showProgress();
    }

    @Override
    public void onNext(AudioData value) {
        if(value.getPrimaryData().size()==0)
            ((AudioListActivity)getActivityContext()).getNoContentFoundLayout().setVisibility(View.VISIBLE);
        presenter.getModel().setData(value);
        setData(value.getPrimaryData());
    }

    @Override
    public void onError(Throwable e) {
        ((AudioListActivity)getActivityContext()).hideProgress();
        isRefreshing = false;
        showError(e.getCause(), isRefreshing);
        contentView.setRefreshing(false);
        e.printStackTrace();
        setData(new ArrayList<>());
    }

    @Override
    public void onComplete() {
        ((AudioListActivity)getActivityContext()).hideProgress();
        ((AudioListActivity)getActivityContext()).updateButtonsVisibility();
        if(isRefreshing)
        contentView.setRefreshing(false);
    }
}