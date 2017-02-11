package ru.skyeng.listening.AudioFiles;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.skyeng.listening.CommonCoponents.BaseActivity;
import ru.skyeng.listening.R;

public class AudioListActivity extends BaseActivity {

    private static final String TAG_AUDIO_FILES_FRAGMENT = "mAudioListFragment";
    @BindView(R.id.appBarLayout)
    AppBarLayout mAppBarLayout;
    AudioListFragment mAudioListFragment;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mAudioListFragment != null) {
            getSupportFragmentManager().putFragment(outState, TAG_AUDIO_FILES_FRAGMENT, mAudioListFragment);
        }
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
    }

}
