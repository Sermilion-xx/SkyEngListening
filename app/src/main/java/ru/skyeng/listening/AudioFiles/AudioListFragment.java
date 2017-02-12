package ru.skyeng.listening.AudioFiles;

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

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.skyeng.listening.AudioFiles.domain.AudioData;
import ru.skyeng.listening.AudioFiles.domain.AudioFile;
import ru.skyeng.listening.AudioFiles.domain.AudioFilesRequestParams;
import ru.skyeng.listening.CommonCoponents.SEApplication;
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

    private boolean isRefreshing;
    private boolean isPlaying;

    @Override
    @Inject
    public void setPresenter(@NonNull AudioListPresenter presenter) {
        presenter.setObserver(this);
        super.setPresenter(presenter);
    }

    @Inject
    void setModel(AudioListModel mode) {
        presenter.setModel(mode);
    }

    @Inject
    AudioListAdapter mAdapter;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.loadingView)
    ProgressBar mProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ((SEApplication) getAppContext()).getAudioListDiComponent().inject(this);
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
        mAdapter.setContext(getActivityContext());
        mAdapter.setFragment(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(this.mRecyclerView.getContext(), layoutManager.getOrientation());
        this.mRecyclerView.addItemDecoration(mDividerItemDecoration);
        if (((AudioListModel) presenter.getModel()).getItems() == null) {
            loadData(false);
        }
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void showPlayer(AudioFile item) {
        ((AudioListActivity) getActivity()).showPlayer(item);
    }

    public void pausePlayer() {
        ((AudioListActivity) getActivity()).pausePlayer();
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
        return "Неизвестная ошибка";
    }

    @NonNull
    @Override
    public AudioListPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void setData(List<AudioFile> data) {
        mAdapter.setAudioFiles(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        if (!pullToRefresh)
            mProgress.setVisibility(View.GONE);
        presenter.loadData(pullToRefresh, new AudioFilesRequestParams());
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
        if (!isRefreshing)
            mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onNext(AudioData value) {
        presenter.getModel().setData(value);
        isRefreshing = false;
        setData(value.getAudioFiles());
    }

    @Override
    public void onError(Throwable e) {
        isRefreshing = false;
        showError(e.getCause(), isRefreshing);
        contentView.setRefreshing(false);
    }

    @Override
    public void onComplete() {
        mProgress.setVisibility(View.GONE);
        contentView.setRefreshing(isRefreshing);
    }


}
