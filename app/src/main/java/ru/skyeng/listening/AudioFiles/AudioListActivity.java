package ru.skyeng.listening.AudioFiles;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.skyeng.listening.AudioFiles.domain.AudioFile;
import ru.skyeng.listening.CommonCoponents.BaseActivity;
import ru.skyeng.listening.R;

public class AudioListActivity extends BaseActivity {

    private static final String TAG_AUDIO_FILES_FRAGMENT = "mAudioListFragment";
    private static final String KEY_AUDIO_FILE = "mAudioFile";
    @BindView(R.id.appBarLayout)
    AppBarLayout mAppBarLayout;
    AudioListFragment mAudioListFragment;
    @BindView(R.id.player_dialog)
    RelativeLayout mLayoutBottomSheet;
    private BottomSheetBehavior mBottomSheetBehavior;

    private ImageView audioCoverImage;
    private TextView audioTitle;
    private TextView audioLeft;
    private SeekBar audioSeek;
    private TextView audioPlayed;
    private TextView audioSubtitles;
    private ImageView audioPlayPause;
    private AudioFile mAudioFile;


    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mAudioListFragment != null) {
            getSupportFragmentManager().putFragment(outState, TAG_AUDIO_FILES_FRAGMENT, mAudioListFragment);
        }
        outState.putParcelable(KEY_AUDIO_FILE, mAudioFile);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupToolbar(getString(R.string.Listening), false);
        mAudioListFragment = (AudioListFragment) setupRecyclerFragment(
                savedInstanceState,
                AudioListFragment.class.getName(),
                R.id.fragment_container
        );
        mBottomSheetBehavior = BottomSheetBehavior.from(mLayoutBottomSheet);

        initPlayerViews(savedInstanceState);
    }

    private void initPlayerViews(Bundle savedInstanceState) {
        audioCoverImage = (ImageView) mLayoutBottomSheet.findViewById(R.id.audio_cover);
        audioTitle = (TextView) mLayoutBottomSheet.findViewById(R.id.audio_title);
        audioLeft = (TextView) mLayoutBottomSheet.findViewById(R.id.audio_left);
        audioPlayed = (TextView) mLayoutBottomSheet.findViewById(R.id.audio_played);
        audioSubtitles = (TextView) mLayoutBottomSheet.findViewById(R.id.audio_subtitles);
        audioSeek = (SeekBar) mLayoutBottomSheet.findViewById(R.id.audio_seek);
        audioPlayPause = (ImageView) mLayoutBottomSheet.findViewById(R.id.audio_play_pause);
        audioPlayPause.setOnClickListener(v -> mAudioListFragment.mAdapter.pausePlayer());
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_AUDIO_FILE)) {
            mAudioFile = savedInstanceState.getParcelable(KEY_AUDIO_FILE);
            if (mAudioFile != null)
                startPlaying(mAudioFile);
        }
    }

    public void startPlaying(AudioFile item) {
        mAudioFile = item;
        audioTitle.setText(item.getTitle());
        audioPlayed.setText(getString(R.string.audio_start_time));
        audioLeft.setText("-" + item.getDurationInMinutes());
        if (item.getImageBitmap() != null) {
            audioCoverImage.setImageBitmap(item.getImageBitmap());
        }
        audioPlayPause.setVisibility(View.VISIBLE);
        int playPauseIcon = R.drawable.ic_play_white;
        if (item.isPlaying()) {
            playPauseIcon = R.drawable.ic_pause_white;
        }
        audioPlayPause.setImageDrawable(ContextCompat.getDrawable(this, playPauseIcon));
        showPlayer();
    }

    public void showPlayer(){
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void pausePlayer(int icon) {
        audioPlayPause.setImageDrawable(ContextCompat.getDrawable(this, icon));
    }

    public void hidePlayer() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    public void foldPlayer() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

}
