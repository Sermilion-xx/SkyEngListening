package ru.skyeng.listening.AudioFiles;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.skyeng.listening.AudioFiles.model.AudioFile;
import ru.skyeng.listening.AudioFiles.player.ComponentListener;
import ru.skyeng.listening.AudioFiles.player.PlayerService;
import ru.skyeng.listening.CommonComponents.BaseActivity;
import ru.skyeng.listening.R;

import static ru.skyeng.listening.AudioFiles.player.PlayerService.ACTION_AUDIO_STATE;
import static ru.skyeng.listening.AudioFiles.player.PlayerService.ACTION_CONTINUE;
import static ru.skyeng.listening.AudioFiles.player.PlayerService.ACTION_PAUSE;
import static ru.skyeng.listening.AudioFiles.player.PlayerService.ACTION_PLAY;
import static ru.skyeng.listening.AudioFiles.player.PlayerService.EXTRA_AUDIO_URL;
import static ru.skyeng.listening.AudioFiles.player.PlayerService.KEY_PLAYER_STATE;
import static ru.skyeng.listening.AudioFiles.player.PlayerService.PROGRESS_BAR_MAX;

public class AudioListActivity extends BaseActivity {

    private static final String TAG_AUDIO_FILES_FRAGMENT = AudioListFragment.class.getName();
    private static final String KEY_AUDIO_FILE = "mAudioFile";
    private static final String KEY_PROGRESS_VISIBILITY = "progressVisibility";

    private AudioListFragment mFragment;
    private BottomSheetBehavior mBottomSheetBehavior;
    private AudioFile mAudioFile;
    private boolean broadcastUpdateFinished;
    private AudioReceiver mPlayerBroadcast;
    private ComponentListener mComponentListener;

    @BindView(R.id.appBarLayout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.player_dialog)
    RelativeLayout mLayoutBottomSheet;
    @BindView(R.id.audio_cover)
    ImageView audioCoverImage;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupToolbar(getString(R.string.Listening), false);
        mFragment = (AudioListFragment) setupRecyclerFragment(
                savedInstanceState,
                TAG_AUDIO_FILES_FRAGMENT,
                R.id.fragment_container
        );
        mBottomSheetBehavior = BottomSheetBehavior.from(mLayoutBottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        setupPlayPlayer();
        restoreSavedInstanceState(savedInstanceState);
        mProgress.setIndeterminate(true);
        mProgress.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorWhite), android.graphics.PorterDuff.Mode.MULTIPLY);

        audioSeek.setOnSeekBarChangeListener(mComponentListener);
        audioSeek.setMax(PROGRESS_BAR_MAX);
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mFragment != null) {
            getSupportFragmentManager().putFragment(outState, TAG_AUDIO_FILES_FRAGMENT, mFragment);
        }
        outState.putParcelable(KEY_AUDIO_FILE, mAudioFile);
        outState.putInt(KEY_PROGRESS_VISIBILITY, mProgress.getVisibility());
        super.onSaveInstanceState(outState);
    }

    private void restoreSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mAudioFile = savedInstanceState.getParcelable(KEY_AUDIO_FILE);
            if (mAudioFile != null) {
                startPlaying(mAudioFile, false);
            }
            int visibility = savedInstanceState.getInt(KEY_PROGRESS_VISIBILITY, -1);
            if (!broadcastUpdateFinished) {
                if (visibility == View.VISIBLE) {
                    mProgress.setVisibility(View.VISIBLE);
                    audioPlayPause.setVisibility(View.GONE);
                } else if (visibility == View.GONE) {
                    mProgress.setVisibility(View.GONE);
                    audioPlayPause.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    private void setupPlayPlayer() {
        audioCoverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFragment.mAdapter.getPlayingPosition() != -1) {
                    int audioState = mFragment.getPresenter().getData().get(mFragment.mAdapter.getPlayingPosition()).getState();
                    int buttonIcon = R.drawable.ic_pause_white;
                    if (audioState == 1) {
                        buttonIcon = R.drawable.ic_play_white;
                        pausePlayerIntent(buttonIcon);
                        mFragment.mAdapter.setPlayerState(2);
                    } else {
                        startPlayerIntent(mAudioFile);
                        mFragment.mAdapter.setPlayerState(1);
                    }
                    if (audioState == 0) {
                        mDarkLayer.setVisibility(View.GONE);
                        audioPlayPause.setVisibility(View.VISIBLE);
                    } else {
                        mDarkLayer.setVisibility(View.VISIBLE);
                    }
                    audioPlayPause.setImageDrawable(ContextCompat.getDrawable(AudioListActivity.this, buttonIcon));
                }
            }
        });
    }

    public void startPlaying(AudioFile item, boolean isNew) {
        try {
            if (isNew) {
                startPlayerIntent(item);
            }
            mDarkLayer.setVisibility(View.VISIBLE);
            mAudioFile = item;
            audioTitle.setText(item.getTitle());
            audioPlayed.setText(getString(R.string.audio_default_time));
            audioLeft.setText("-" + item.getDurationInMinutes());
            if (item.getImageBitmap() != null) {
                audioCoverImage.setImageBitmap(item.getImageBitmap());
            }
            audioPlayPause.setVisibility(View.VISIBLE);
            int playPauseIcon = R.drawable.ic_play_white;
            if (item.getState() == 1) {
                playPauseIcon = R.drawable.ic_pause_white;
            }
            audioPlayPause.setImageDrawable(ContextCompat.getDrawable(this, playPauseIcon));
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startPlayerIntent(AudioFile item) {
        if (item.compareTo(mAudioFile) == 0) {
            Intent continueIntent = new Intent(ACTION_CONTINUE);
            sendBroadcast(continueIntent);
        } else {
            Intent intent = new Intent(this, PlayerService.class);
            intent.setAction(ACTION_PLAY);
            intent.putExtra(EXTRA_AUDIO_URL, item.getAudioFileUrl());
            startService(intent);
        }
    }

    public void pausePlayerIntent(int icon) {
        Intent intent = new Intent(ACTION_PAUSE);
        sendBroadcast(intent);
        audioPlayPause.setImageDrawable(ContextCompat.getDrawable(this, icon));
    }


    public void updateButtonsVisibility() {
        mLengthButton.setText(getString(R.string.length));
        mCategoryButton.setText(getString(R.string.categories));
    }

    private class AudioReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra(KEY_PLAYER_STATE, false)) {
                mProgress.setVisibility(View.VISIBLE);
                audioPlayPause.setVisibility(View.GONE);
            } else {
                mProgress.setVisibility(View.GONE);
                audioPlayPause.setVisibility(View.VISIBLE);
            }
            broadcastUpdateFinished = true;
        }
    }



    @Override
    public void onResume() {
        mPlayerBroadcast = new AudioReceiver();
        registerReceiver(mPlayerBroadcast, new IntentFilter(ACTION_AUDIO_STATE));
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mPlayerBroadcast != null) {
            unregisterReceiver(mPlayerBroadcast);
        }
        super.onPause();
    }

}
