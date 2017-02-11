package ru.skyeng.listening.AudioFiles;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.skyeng.listening.AudioFiles.domain.AudioFile;
import ru.skyeng.listening.AudioFiles.domain.AudioFilesRequestParams;
import ru.skyeng.listening.CommonCoponents.HidingScrollListener;
import ru.skyeng.listening.MVPBase.MVPView;
import ru.skyeng.listening.R;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class AudioListFragment extends MvpLceFragment<
        SwipeRefreshLayout,
        List<AudioFile>,
        MVPView<List<AudioFile>>,
        AudioListPresenter>
        implements MVPView<List<AudioFile>>,
        SwipeRefreshLayout.OnRefreshListener {


    private AppBarLayout appBarLayout;

    public void setAppBarLayout(AppBarLayout appBarLayout) {
        this.appBarLayout = appBarLayout;
    }

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.loadingView)
    ProgressBar mProgress;
    AudioListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_audio_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);
        setRetainInstance(true);
        ButterKnife.bind(this, view);
        contentView.setOnRefreshListener(this);
        mAdapter = new AudioListAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(this.mRecyclerView.getContext(), layoutManager.getOrientation());
        this.mRecyclerView.addItemDecoration(mDividerItemDecoration);

        mRecyclerView.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }

            @Override
            public void onShow() {
                showViews();
            }
        });
        loadData(false);
    }

    private void hideViews() {
        appBarLayout.animate().translationY(-appBarLayout.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    private void showViews() {
        appBarLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.getMessage();
    }

    @NonNull
    @Override
    public AudioListPresenter createPresenter() {
        return new AudioListPresenter();
    }

    @Override
    public void setData(List<AudioFile> data) {
        mAdapter.setAudioFiles(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        if (!pullToRefresh)
            mProgress.setVisibility(View.VISIBLE);
        presenter.loadData(pullToRefresh, new AudioFilesRequestParams());
    }

    @Override
    public void showContent() {
        mProgress.setVisibility(View.GONE);
        super.showContent();
        contentView.setRefreshing(false);
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        mProgress.setVisibility(View.GONE);
        super.showError(e, pullToRefresh);
        contentView.setRefreshing(false);
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        super.showLoading(pullToRefresh);
        contentView.setRefreshing(pullToRefresh);
    }

    @Override
    public Context getAppContext() {
        return getActivity();
    }

    @Override
    public Context getActivityContext() {
        return getActivity().getApplicationContext();
    }
}
