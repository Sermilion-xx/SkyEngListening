package ru.skyeng.listening.Modules.AudioFiles;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.makeramen.roundedimageview.RoundedImageView;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.skyeng.listening.CommonComponents.BaseActivity;
import ru.skyeng.listening.CommonComponents.FacadeCommon;
import ru.skyeng.listening.Modules.AudioFiles.model.AudioFile;
import ru.skyeng.listening.Modules.AudioFiles.model.AudioFilesRequestParams;
import ru.skyeng.listening.Modules.AudioFiles.model.SubtitleFile;
import ru.skyeng.listening.Modules.AudioFiles.player.PlayerService;
import ru.skyeng.listening.Modules.Categories.CategoriesActivity;
import ru.skyeng.listening.Modules.Settings.SettingsActivity;
import ru.skyeng.listening.Modules.Settings.model.SettingsObject;
import ru.skyeng.listening.R;
import ru.skyeng.listening.Utility.FacadePreferences;
import ru.skyeng.listening.Utility.asynctask.CommonAsyncTask;

import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.ACTION_AUDIO_STATE;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.ACTION_DID_NOT_STAR;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.BINDER_MESSENGER;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.EXTRA_AUDIO_URL;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.KEY_PLAYER_STATE;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.MESSAGE_PLAYBACK_TIME;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.MESSAGE_SUBTITLE_TIME;

public class AudioListActivity extends BaseActivity implements Observer<List<SubtitleFile>> {

    private static final String TAG_AUDIO_FILES_FRAGMENT = AudioListFragment.class.getName();
    private static final String KEY_AUDIO_FILE = "mAudioFile";
    private static final String KEY_PROGRESS_VISIBILITY = "progressVisibility";
    public static final int TAG_REQUEST_CODE = 0;
    public static final String TAG_REQUEST_DATA = "tagExtra";
    private static final String KEY_SERVICE_BOUND = "serviceBound";
    private static final String KEY_SUBTITLE_ENGINE = "mSubtitleEngine";

    public static boolean categoriesSelected = false;

    private AudioListFragment mFragment;
    private BottomSheetBehavior mBottomSheetBehavior;
    private AudioFile mAudioFile;
    private List<Integer> mSelectedTags;
    private SubtitleEngine mSubtitleEngine;

    private boolean broadcastUpdateFinished;
    private AudioReceiver mPlayerBroadcast;

    boolean mBound = false;
    private Messenger msgService;

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

    public RelativeLayout getNoContentFoundLayout() {
        return mNoContentFoundLayout;
    }

    public AudioFile getAudioFile() {
        return mAudioFile;
    }

    public void setAudioFile(AudioFile mAudioFile) {
        this.mAudioFile = mAudioFile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        startService(new Intent(this, PlayerService.class));
        setupToolbar(getString(R.string.Listening));
        mFragment = (AudioListFragment) setupRecyclerFragment(
                savedInstanceState,
                AudioListFragment.class,
                R.id.fragment_container
        );
        mSubtitleEngine = new SubtitleEngine();
        mBottomSheetBehavior = BottomSheetBehavior.from(mLayoutBottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        restoreSavedInstanceState(savedInstanceState);
        setupPlayerCoverListener();
        mProgress = (ProgressBar) findViewById(R.id.loadingView);
        mAudioProgressBar.setIndeterminate(true);
        mAudioProgressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorWhite), android.graphics.PorterDuff.Mode.MULTIPLY);
        setupListeners();
    }

    private void setupListeners() {
        mCategoryButton.setOnClickListener(
                v -> {
                    Intent intent = new Intent(AudioListActivity.this, CategoriesActivity.class);
                    Gson gson = new Gson();
                    String jsonTags = gson.toJson(mSelectedTags);
                    intent.putExtra(TAG_REQUEST_DATA, jsonTags);
                    startActivityForResult(intent, TAG_REQUEST_CODE);
                });

        mLengthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsObject settings = FacadePreferences.getSettingsFromPref(AudioListActivity.this);
                showDurationPicker(settings);
            }
        });


        mResetCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragment.setRequestParams(new AudioFilesRequestParams());
                mNoContentFoundLayout.setVisibility(View.GONE);
                mFragment.loadData(false);
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
                sendMessage(null, PlayerService.MESSAGE_PLAYBACK_SEARCH, (long) currentProgress * 1000);
            }
        });
        mSettingsButton.setOnClickListener(v -> startActivity(new Intent(AudioListActivity.this, SettingsActivity.class)));
    }

    private void showDurationPicker(SettingsObject settings) {
        if (settings == null) {
            settings = new SettingsObject();
        }
        CharSequence durations[] = new CharSequence[]{
                "От 0 до 5 минут",
                "От 5 до 10 минут",
                "От 10 до 20 минут",
                "От 20 и больше минут"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AudioListActivity.this);
        SettingsObject finalSettings = settings;
        boolean[] selected = settings.getDurationsBooleanArray();
        builder.setTitle(R.string.select_duration)
                .setMultiChoiceItems(durations, selected, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        selected[which] = isChecked;
                    }
                })
                .setPositiveButton(R.string.select, (dialog, id) -> {
                            finalSettings.setDuration(selected);
                            CommonAsyncTask<Void, Void, Void> saveSettingsTask = new CommonAsyncTask<>();
                            saveSettingsTask.setDoInBackground(param -> {
                                FacadePreferences.setSettingsToPref(AudioListActivity.this, finalSettings);
                                return null;
                            });
                            saveSettingsTask.setConsumer(param -> {
                                showToast(R.string.settings_saved);
                                mFragment.loadData(false);
                            });
                            saveSettingsTask.execute();
                        }
                ).setCancelable(true)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAG_REQUEST_CODE && resultCode == RESULT_OK) {
            mNoContentFoundLayout.setVisibility(View.GONE);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Integer>>() {
            }.getType();
            mSelectedTags = gson.fromJson(data.getStringExtra(TAG_REQUEST_DATA), type);
            AudioFilesRequestParams params = new AudioFilesRequestParams();
            params.setTagIds(mSelectedTags);
            mFragment.setRequestParams(params);
            mFragment.loadData(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mFragment != null) {
            getSupportFragmentManager().putFragment(outState, TAG_AUDIO_FILES_FRAGMENT, mFragment);
        }
        outState.putParcelable(KEY_SUBTITLE_ENGINE, mSubtitleEngine);
        outState.putBoolean(KEY_SERVICE_BOUND, mBound);
        outState.putParcelable(KEY_AUDIO_FILE, mAudioFile);
        outState.putInt(KEY_PROGRESS_VISIBILITY, mAudioProgressBar.getVisibility());
        super.onSaveInstanceState(outState);
    }


    private void restoreSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mSubtitleEngine = savedInstanceState.getParcelable(KEY_SUBTITLE_ENGINE);
            mBound = savedInstanceState.getBoolean(KEY_SERVICE_BOUND);
            mAudioFile = savedInstanceState.getParcelable(KEY_AUDIO_FILE);
            int visibility = savedInstanceState.getInt(KEY_PROGRESS_VISIBILITY, -1);
            if (!broadcastUpdateFinished) {
                updatePlayerUI();
            }
        }
    }

    private void setupPlayerCoverListener() {
        audioCoverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFragment.mAdapter.getPlayingPosition() != -1) {
                    int audioState = mAudioFile.getState();
                    int icon;
                    int state;
                    if (audioState == 1) {
                        state = 2;
                        icon = R.drawable.ic_play_white;
                        pausePlayerMessage();
                    } else {
                        state = 1;
                        icon = R.drawable.ic_pause_white;
                        continuePlayingMessage();
                    }
                    mFragment.mAdapter.setPlayerState(state);
                    audioPlayPause.setImageDrawable(ContextCompat.getDrawable(AudioListActivity.this, icon));
                    if (audioState == 0) {
                        mDarkLayer.setVisibility(View.GONE);
                        audioPlayPause.setVisibility(View.VISIBLE);
                    } else {
                        mDarkLayer.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void updatePlayerUI() {
        if (mAudioFile != null) {
            audioTitle.setText(mAudioFile.getTitle());
            audioSeek.setMax(mAudioFile.getDurationInSeconds());
            if (mAudioFile.getImageBitmap() != null) {
                audioCoverImage.setImageBitmap(mAudioFile.getImageBitmap());
            }
            int icon = R.drawable.ic_pause_white;
            if (mAudioFile.getState() == 1 && !mAudioFile.isLoading()) {
                hideAudioLoading();
                audioPlayPause.setVisibility(View.VISIBLE);
            } else if (mAudioFile.getState() == 2 && !mAudioFile.isLoading()) {
                icon = R.drawable.ic_play_white;
                hideAudioLoading();
                audioPlayPause.setVisibility(View.VISIBLE);
            } else {
                showAudioLoading();
                audioPlayPause.setVisibility(View.GONE);
            }
            mDarkLayer.setVisibility(View.VISIBLE);
            audioPlayPause.setImageDrawable(ContextCompat.getDrawable(this, icon));
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    private void hideAudioLoading() {
        mAudioProgressBar.setVisibility(View.GONE);
    }

    private void showAudioLoading() {
        mAudioProgressBar.setVisibility(View.VISIBLE);
    }

    public void startPlayerMessage() {
        sendMessage(null, PlayerService.MESSAGE_PLAY);
    }

    public void startBufferingMessage(AudioFile audioFile) {
        mSubtitleEngine = new SubtitleEngine();
        audioSubtitles.setText(getString(R.string.dash));
        mAudioFile = audioFile;
        mAudioFile.setLoading(true);
        updatePlayerUI();
        bindPlayerService();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_AUDIO_URL, audioFile.getAudioFileUrl());
        sendMessage(bundle, PlayerService.MESSAGE_START_BUFFERING);
    }

    public void continuePlayingMessage() {
        updatePlayerUI();
        if (mAudioFile != null)
            mAudioFile.setState(1);
        sendMessage(null, PlayerService.MESSAGE_CONTINUE);
    }

    public void pausePlayerMessage() {
        if (mAudioFile != null)
            mAudioFile.setState(2);
        updatePlayerUI();
        sendMessage(null, PlayerService.MESSAGE_PAUSE);
    }

    public void updateButtonsVisibility() {
        mLengthButton.setText(getString(R.string.length));
        mCategoryButton.setText(getString(R.string.categories));
    }

    @Override
    public void onResume() {
        if(mFragment.modelHasData()){
            updateButtonsVisibility();
        }
        if (mAudioFile != null) {
            if (mAudioFile.isLoading()) {
                mAudioFile.setLoading(false);
                mAudioFile.setState(1);
                updatePlayerUI();
            }
        }
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
            unbindService(playerConnection);
            mBound = false;
        }
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindPlayerService();
    }

    //Binding к PlayerService
    public void sendMessage(Bundle bundle, int type, Object... obj) {
        if (mBound) {
            try {
                Message message = Message.obtain(null, type, 1, 1);
                if (obj.length != 0) {
                    message.obj = obj[0];
                }
                message.setData(bundle);
                msgService.send(message); //sending message to service
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public ServiceConnection playerConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder binder) {
            mBound = true;
            msgService = new Messenger(binder);
        }

        public void onServiceDisconnected(ComponentName className) {
            mBound = false;
        }
    };

    public Handler playbackHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.what == MESSAGE_PLAYBACK_TIME) {
                long time = (long) message.obj / 1000;
                audioSeek.setProgress((int) time);
                audioPlayed.setText(FacadeCommon.getDateFromMillis((Long) message.obj));
                long duration = mAudioFile.getDurationInSeconds();
                audioLeft.setText(String.format(getString(R.string.leftTime), FacadeCommon.getDateFromMillis(duration * 1000 - (long) message.obj)));
            } else if (message.what == MESSAGE_SUBTITLE_TIME) {
                long time = (long) message.obj * 1000;
                if (mSubtitleEngine.size() > 0)
                    audioSubtitles.setText(mSubtitleEngine.updateSubtitles(time).getTextEn());
            }
        }
    };

    public void bindPlayerService() {
        if (!mBound) {
            Intent intent = new Intent(this, PlayerService.class);
            Messenger messenger = new Messenger(playbackHandler);
            intent.putExtra(BINDER_MESSENGER, messenger);
            bindService(intent, playerConnection, Context.BIND_AUTO_CREATE);
        }
    }

    //Broadcast получаемый при начале воспроизведения аудио
    private class AudioReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_AUDIO_STATE)) {
                mAudioFile.setLoading(intent.getBooleanExtra(KEY_PLAYER_STATE, false));
                updatePlayerUI();
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

    //Callback методы запроса на субтитры
    @Override
    public void onSubscribe(Disposable d) {
        System.out.println();
    }

    @Override
    public void onNext(List<SubtitleFile> value) {
        mSubtitleEngine.setSubtitles(value);
        startPlayerMessage();
    }

    @Override
    public void onError(Throwable e) {
        System.out.println();
    }

    @Override
    public void onComplete() {
        System.out.println();
    }

}
