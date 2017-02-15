package ru.skyeng.listening.AudioFiles;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.skyeng.listening.AudioFiles.model.AudioData;
import ru.skyeng.listening.AudioFiles.model.AudioFile;
import ru.skyeng.listening.CommonComponents.SEApplication;
import ru.skyeng.listening.CommonComponents.BaseFragment;
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

public class AudioListFragment extends BaseFragment<AudioFile, AudioListPresenter, AudioListAdapter, AudioData> {

    @Override
    @Inject
    public void setPresenter(@NonNull AudioListPresenter presenter) {
        presenter.setObserver(this);
        super.setPresenter(presenter);
    }

    @Inject
    void setModel(AudioListModel model) {
        presenter.setModel(model);
    }

    @Inject
    public void setAdapter(AudioListAdapter adapter){
        mAdapter = adapter;
    }

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

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
        ((AudioListActivity) getActivity()).startPlaying(item, true);
    }

    public void pausePlayer(int icon) {
        ((AudioListActivity) getActivity()).pausePlayerIntent(icon);
    }

    @Override
    public void onComplete() {
        ((AudioListActivity) getActivity()).updateButtonsVisibility();
        ((AudioListActivity) getActivity()).hideProgress();
        contentView.setRefreshing(isRefreshing);
    }
}
