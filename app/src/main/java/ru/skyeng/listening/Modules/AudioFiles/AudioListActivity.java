package ru.skyeng.listening.Modules.AudioFiles;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.makeramen.roundedimageview.RoundedImageView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.skyeng.listening.CommonComponents.BaseActivity;
import ru.skyeng.listening.CommonComponents.EndlessRecyclerViewScrollListener;
import ru.skyeng.listening.CommonComponents.FacadeCommon;
import ru.skyeng.listening.CommonComponents.FilterSingleton;
import ru.skyeng.listening.CommonComponents.Interfaces.MVPBase.MVPView;
import ru.skyeng.listening.CommonComponents.PlayerCallback;
import ru.skyeng.listening.CommonComponents.SEApplication;
import ru.skyeng.listening.Modules.AudioFiles.model.AudioFile;
import ru.skyeng.listening.Modules.AudioFiles.model.SubtitlesRequestParams;
import ru.skyeng.listening.Modules.AudioFiles.player.PlayerService;
import ru.skyeng.listening.Modules.AudioFiles.player.PlayerState;
import ru.skyeng.listening.Modules.Categories.CategoriesActivity;
import ru.skyeng.listening.Modules.Settings.SettingsActivity;
import ru.skyeng.listening.R;

import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.ACTION_AUDIO_STATE;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.ACTION_DID_NOT_STAR;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.EXTRA_AUDIO_URL;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.KEY_CURRENT_AUDIO;

public class AudioListActivity extends BaseActivity<MVPView, AudioListPresenter> implements SwipeRefreshLayout.OnRefreshListener, MVPView {

    private static final String KEY_PROGRESS_VISIBILITY = "progressVisibility";
    public static final int TAG_REQUEST_CODE = 0;
    public static final String TAG_REQUEST_DATA = "tagExtra";
    private static final String KEY_SERVICE_BOUND = "serviceBound";
    public static final String ACTION_UPDATE_ADAPTER = "updateAdapter";
    private static final String CATEGORY_BUTTON_TEXT = "categoryButtonText";

    class EndlessScrollListener extends EndlessRecyclerViewScrollListener {

        public EndlessScrollListener(LinearLayoutManager layoutManager) {
            super(layoutManager);
        }

        @Override
        public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
            presenter.loadMore(totalItemsCount);
        }
    }

    @Override
    @Inject
    public void setPresenter(@NonNull AudioListPresenter presenter) {
        this.presenter = presenter;
    }

    public static boolean categoriesSelected = false;
    private BottomSheetBehavior mBottomSheetBehavior;

    private boolean broadcastUpdateFinished;
    private AudioReceiver mPlayerBroadcast;
    boolean mBound = false;
    private EndlessScrollListener mScrollListener;
    private AudioListAdapter mAdapter;
    private FilterSingleton mFilter;

    @BindView(R.id.appBarLayout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.player_dialog)
    RelativeLayout mLayoutBottomSheet;
    @BindView(R.id.audio_cover)
    RoundedImageView audioCoverImage;
    @BindView(R.id.audio_title)
    TextView audioTitle;
    @BindView(R.id.audio_left)
    TextView audioLeft;
    @BindView(R.id.audio_seek)
    SeekBar audioSeek;
    @BindView(R.id.audio_played)
    TextView audioPlayed;
    @BindView(R.id.audio_subtitles)
    TextView audioSubtitles;
    @BindView(R.id.audio_play_pause)
    ImageView audioPlayPause;
    @BindView(R.id.cover_dark_layer)
    View mDarkLayer;
    @BindView(R.id.button_length)
    Button mLengthButton;
    @BindView(R.id.button_category)
    Button mCategoryButton;
    @BindView(R.id.progressBar)
    ProgressBar mAudioProgressBar;
    @BindView(R.id.no_content_found)
    RelativeLayout mNoContentFoundLayout;
    @BindView(R.id.text_try)
    TextView mResetCategories;
    @BindView(R.id.action_settings)
    ImageButton mSettingsButton;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.player_expand_button)
    ImageView mExpandPlayerButton;

    public void hideNoContentView() {
        mNoContentFoundLayout.setVisibility(View.GONE);
    }

    public void showNoContentView() {
        mNoContentFoundLayout.setVisibility(View.VISIBLE);
    }

    public boolean modelHasData() {
        if (presenter != null && presenter.getData() != null) {
            return presenter.getData().size() > 0;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((SEApplication) getApplicationContext()).getAudioListDiComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgress = (ProgressBar) findViewById(R.id.loadingView);
        this.presenter.injectDependencies();
        setupToolbar(getString(R.string.Listening));
        mFilter = FilterSingleton.getInstance();
        ButterKnife.bind(this);
        mResetCategories.setText(Html.fromHtml(getResources().getString(R.string.try_to_refresh)));
        mAdapter = new AudioListAdapter(this, new PlayerCallback() {
            @Override
            public void startPlaying(AudioFile audioFile) {
                presenter.loadSubtitles(new SubtitlesRequestParams(audioFile.getId()));
                startBufferingMessage(audioFile);
            }

            @Override
            public void pausePlayer() {
                pausePlayerMessage();
            }

            @Override
            public void continuePlaying() {
                continuePlayingMessage();
            }
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mScrollListener = new EndlessScrollListener(mLayoutManager);
        mRecyclerView.addOnScrollListener(mScrollListener);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(this.mRecyclerView.getContext(), mLayoutManager.getOrientation());
        this.mRecyclerView.addItemDecoration(mDividerItemDecoration);
        swipeContainer.setColorSchemeResources(R.color.colorAccent);
        swipeContainer.setOnRefreshListener(this);

        startService(new Intent(this, PlayerService.class));

        mBottomSheetBehavior = BottomSheetBehavior.from(mLayoutBottomSheet);
        mBottomSheetBehavior.setPeekHeight(225);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mLayoutBottomSheet.setBackground(ContextCompat.getDrawable(this, R.drawable.left_right_gradient_blue));
        audioCoverImage.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
        audioCoverImage.setImageResource(R.drawable.ic_player_cover);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == 3){
                    audioTitle.setTextColor(ContextCompat.getColor(AudioListActivity.this, R.color.textColorDark));
                    mExpandPlayerButton.setVisibility(View.GONE);
                    mLayoutBottomSheet.setBackgroundColor(ContextCompat.getColor(AudioListActivity.this, R.color.almostWhite));
                } else if(newState == 4){
                    audioTitle.setTextColor(ContextCompat.getColor(AudioListActivity.this, R.color.colorWhite));
                    mExpandPlayerButton.setVisibility(View.VISIBLE);
                    mLayoutBottomSheet.setBackground(ContextCompat.getDrawable(AudioListActivity.this, R.drawable.left_right_gradient_blue));
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        restoreSavedInstanceState(savedInstanceState);
        //TriggersMethod("setupPlayerCoverListener")
        presenter.sendMessage(null, PlayerService.MESSAGE_PLAYING_FILE_STATE_FOR_COVER);
        mProgress = (ProgressBar) findViewById(R.id.loadingView);
        mAudioProgressBar.setIndeterminate(true);
        mAudioProgressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorWhite), android.graphics.PorterDuff.Mode.MULTIPLY);
        setupListeners();
        setupPlayerCoverListener();
    }

    private void setupListeners() {
        mExpandPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        mCategoryButton.setOnClickListener(
                v -> {
                    Intent intent = new Intent(AudioListActivity.this, CategoriesActivity.class);
                    startActivityForResult(intent, TAG_REQUEST_CODE);
                });

        mLengthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDurationPicker();
            }
        });

        mResetCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNoContentFoundLayout.setVisibility(View.GONE);
                resetLengthButton();
                resetCategoriesButton();
                mFilter.setDuration(new boolean[4]);
                mFilter.getSelectedTags().clear();
                setNewDurations();
                loadData(false);
            }
        });

        audioSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int currentProgress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                presenter.sendMessage(null, PlayerService.MESSAGE_PLAYBACK_SEARCH, (long) currentProgress * 1000);
            }
        });
        mSettingsButton.setOnClickListener(v -> startActivity(new Intent(AudioListActivity.this, SettingsActivity.class)));
    }

    private void setNewDurations() {
        presenter.getRequestParams().prepareDurations(mFilter.getDuration());
    }

    private void showDurationPicker() {
        CharSequence durations[] = new CharSequence[]{
                getString(R.string.from0to5),
                getString(R.string.from5to10),
                getString(R.string.from10to20),
                getString(R.string.from20andMore)};

        AlertDialog.Builder builder = new AlertDialog.Builder(AudioListActivity.this);
        boolean[] selected = mFilter.getDurationsBooleanArray();//settings.getDurationsBooleanArray();
        builder.setTitle(R.string.select_duration)
                .setMultiChoiceItems(durations, selected, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        selected[which] = isChecked;
                    }
                }).setPositiveButton(R.string.select, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mFilter.setDuration(selected);
                                Pair<Integer, Integer> durationRange = mFilter.getDurationRange();
                                if (mFilter.getDuration().size() > 0) {
                                    if (durationRange.first > -1 && durationRange.second <= 2400) {
                                        if (durationRange.first == 0 && durationRange.second == 2400) {
                                            mLengthButton.setText(R.string.from_0_and_greater);
                                        } else {
                                            if (durationRange.first == 0 && durationRange.second < 2400) {
                                                mLengthButton.setText(String.format(getString(R.string.from_0_to), durationRange.second / 60));
                                            } else if(durationRange.first > 0 && durationRange.second == 2400) {
                                                mLengthButton.setText(String.format(getString(R.string.from_n_and_greater), durationRange.first / 60));
                                            } if (durationRange.first > 0 && durationRange.second < 2400) {
                                                mLengthButton.setText(String.format(getString(R.string.selected_time), durationRange.first / 60, durationRange.second / 60));
                                            }
                                        }
                                        mLengthButton.setBackgroundColor(ContextCompat.getColor(AudioListActivity.this, R.color.colorBlue3));
                                        mLengthButton.setTextColor(ContextCompat.getColor(AudioListActivity.this, R.color.colorWhite));
                                    } else {
                                        resetLengthButton();
                                    }
                                }
                                mFilter.setDuration(selected);
                                presenter.clear();
                                setNewDurations();
                                loadData(false);
                            }
                        }
                ).setCancelable(true)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void resetLengthButton() {
        mLengthButton.setBackgroundColor(ContextCompat.getColor(AudioListActivity.this, R.color.colorBlue1));
        mLengthButton.setText(getString(R.string.length));
        mLengthButton.setTextColor(ContextCompat.getColor(AudioListActivity.this, R.color.colorBlue3));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAG_REQUEST_CODE && resultCode == RESULT_OK) {
            mNoContentFoundLayout.setVisibility(View.GONE);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Integer>>() {
            }.getType();
            mFilter = FilterSingleton.getInstance();
            List<Integer> selected = gson.fromJson(data.getStringExtra(TAG_REQUEST_DATA), type);
            if (selected != null) {
                mFilter.setSelectedTags(selected);
            } else {
                mFilter.setSelectedTags(new ArrayList<>());
            }
            presenter.getRequestParams().setTagIds(mFilter.getSelectedTags());
            getPresenter().clear();
            presenter.getRequestParams().setPage(1);
            presenter.clear();
            if (mFilter.getSelectedTags().size() > 0) {
                mCategoryButton.setText(String.format(getString(R.string.selected_categories), mFilter.getSelectedTags().size()));
                mCategoryButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
                mCategoryButton.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            } else {
                resetCategoriesButton();
            }
            loadData(false);
        }
    }

    private void resetCategoriesButton() {
        mCategoryButton.setText(getString(R.string.categories));
        mCategoryButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlue0));
        mCategoryButton.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(CATEGORY_BUTTON_TEXT, mCategoryButton.getText().toString());
        outState.putBoolean(KEY_SERVICE_BOUND, mBound);
        outState.putInt(KEY_PROGRESS_VISIBILITY, mAudioProgressBar.getVisibility());
        super.onSaveInstanceState(outState);
    }

    private void restoreSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mBound = savedInstanceState.getBoolean(KEY_SERVICE_BOUND);
            mCategoryButton.setText(savedInstanceState.getString(CATEGORY_BUTTON_TEXT));
        }
    }

    @Override
    public void onRefresh() {
        presenter.getRequestParams().setPage(1);
        loadData(true);
    }

    public void updateAdapter(List<AudioFile> data, AudioFile currentFile) {
        if (currentFile != null) {
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getId() == currentFile.getId()) {
                    data.get(i).setState(PlayerState.PLAY);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    public void loadData(boolean pullToRefresh) {
        if (pullToRefresh) {
            presenter.getRequestParams().setPage(1);
            mScrollListener.resetState();
        }
        presenter.loadData(pullToRefresh);
    }

    //--------------------------Player UI----------------------------------------//
    public void setupPlayerCoverListener() {
        audioCoverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.sendMessage(null, PlayerService.MESSAGE_PLAYING_FILE_STATE_FOR_COVER);
            }
        });
    }

    public void onPlayerCoverClick(PlayerState audioState) {
        if (mAdapter.getPlayingPosition() != -1) {
            int icon;
            PlayerState state;
            if (audioState == PlayerState.PLAY) {
                state = PlayerState.PAUSE;
                icon = R.drawable.ic_play_white;
                pausePlayerMessage();
            } else {
                state = PlayerState.PLAY;
                icon = R.drawable.ic_pause_white;
                continuePlayingMessage();
            }
            mAdapter.setPlayerState(state);
            audioPlayPause.setImageDrawable(ContextCompat.getDrawable(AudioListActivity.this, icon));
            if (audioState == PlayerState.STOP) {
                mDarkLayer.setVisibility(View.GONE);
                audioPlayPause.setVisibility(View.VISIBLE);
            } else {
                mDarkLayer.setVisibility(View.VISIBLE);
            }
        }
    }

    public void updatePlayerUI(AudioFile mAudioFile) {
        if (mAudioFile != null) {
            audioTitle.setText(mAudioFile.getTitle());
            audioSeek.setMax(mAudioFile.getDurationInSeconds());
            int icon = R.drawable.ic_pause_white;
            if (mAudioFile.getState() == PlayerState.PLAY && !mAudioFile.isLoading()) {
                hideAudioLoading();
                audioPlayPause.setVisibility(View.VISIBLE);
            } else if (mAudioFile.getState() == PlayerState.PAUSE && !mAudioFile.isLoading()) {
                icon = R.drawable.ic_play_white;
                hideAudioLoading();
                audioPlayPause.setVisibility(View.VISIBLE);
            } else {
                showAudioLoading();
                audioPlayPause.setVisibility(View.GONE);
            }
            mDarkLayer.setVisibility(View.VISIBLE);
            audioPlayPause.setImageDrawable(ContextCompat.getDrawable(this, icon));
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            mLayoutBottomSheet.setBackground(ContextCompat.getDrawable(this, R.drawable.left_right_gradient_blue));
        }
    }

    private void hideAudioLoading() {
        mAudioProgressBar.setVisibility(View.GONE);
    }

    private void showAudioLoading() {
        mAudioProgressBar.setVisibility(View.VISIBLE);
    }

    public void startPlayerMessage() {
        presenter.sendMessage(null, PlayerService.MESSAGE_PLAY);
    }

    public void startBufferingMessage(AudioFile audioFile) {
        Glide.with(this)
                .load(audioFile.getImageFileUrl())
                .asBitmap()
                .priority(Priority.HIGH)
                .placeholder(R.drawable.ic_player_cover)
                .into(audioCoverImage);
        audioSubtitles.setText(getString(R.string.dash));

        audioFile.setLoading(true);
        updatePlayerUI(audioFile);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_AUDIO_URL, audioFile.getAudioFileUrl());
        presenter.sendMessage(bundle, PlayerService.MESSAGE_START_BUFFERING, audioFile);
    }

    public void continuePlayingMessage() {
        presenter.sendMessage(null, PlayerService.MESSAGE_CONTINUE);
    }

    public void pausePlayerMessage() {
        presenter.sendMessage(null, PlayerService.MESSAGE_PAUSE);
    }

    public void updateButtonsVisibility() {
        if (mFilter.getSelectedTags().size() == 0) {
            mCategoryButton.setText(getString(R.string.categories));
        }
        if (mFilter.getDuration().size() == 0) {
            mLengthButton.setText(getString(R.string.length));
        }
    }

    //--------------------------Player UI----------------------------------------//
    //-----------------------Lifecycle Methods-----------------------------------//
    @Override
    public void onResume() {
        if (modelHasData()) {
            updateButtonsVisibility();
        }
//        presenter.sendMessage(null, PlayerService.MESSAGE_PLAYING_FILE_STATE_FOR_COVER);
//        presenter.sendMessage(null, PlayerService.MESSAGE_UPDATE_PLAYER_UI);
        mPlayerBroadcast = new AudioReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_DID_NOT_STAR);
        intentFilter.addAction(ACTION_AUDIO_STATE);
        registerReceiver(mPlayerBroadcast, intentFilter);
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mPlayerBroadcast != null) {
            unregisterReceiver(mPlayerBroadcast);
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (mBound) {
            unbindService(presenter.playerConnection);
            mBound = false;
        }
        super.onStop();
    }

    //--------------------------Lifecycle Methods--------------------------------//
    //---------------------------------------------------------------------------//
    public void updateSubtitles(long time) {
        if (presenter.getSubtitleEngine().size() > 0)
            audioSubtitles.setText(presenter.getSubtitleEngine().updateSubtitles(time * 1000).getTextEn());
    }

    public void updatePlaybacktime(long elapsedTime, long duration) {
        audioSeek.setProgress((int) elapsedTime / 1000);
        audioPlayed.setText(FacadeCommon.getDateFromMillis(elapsedTime));
        audioLeft.setText(String.format(getString(R.string.leftTime), FacadeCommon.getDateFromMillis(duration * 1000 - elapsedTime)));
    }

    public void updateAdapter(AudioFile currentFile) {
        updateAdapter(mAdapter.getItems(), currentFile);
    }

    public void setRefreshing(boolean refreshing) {
        swipeContainer.setRefreshing(refreshing);
    }

    //Broadcast получаемый при начале воспроизведения аудио
    private class AudioReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_AUDIO_STATE)) {
                updatePlayerUI(intent.getParcelableExtra(KEY_CURRENT_AUDIO));
                broadcastUpdateFinished = true;
            } else if (intent.getAction().equals(ACTION_DID_NOT_STAR)) {
                hideAudioLoading();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    public void updatePlayList(List<AudioFile> value, boolean fresh) {
        if (value.size() == 0 && presenter.getRequestParams().getPage() == 1) {
            showNoContentView();
        } else {
            hideNoContentView();
            mAdapter.setPlayingPosition(-1);
        }
        mAdapter.setItems(presenter.getData());
        presenter.sendMessage(null, PlayerService.MESSAGE_UPDATE_ADAPTER);
    }

    public void onError(Throwable e) {
        hideProgress();
        if (presenter.getModel().getItems() == null)
            mNoContentFoundLayout.setVisibility(View.VISIBLE);
        e.printStackTrace();
    }

    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }

    @Override
    public Context getActivityContext() {
        return this;
    }

}
