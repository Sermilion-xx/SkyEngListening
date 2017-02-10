package ru.skyeng.listening.AudioFiles;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;

import ru.skyeng.listening.CommonCoponents.BaseActivity;
import ru.skyeng.listening.R;

public class AudioListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar(getString(R.string.Listening), false);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        AudioListFragment fragment = (AudioListFragment) setupRecyclerFragment(savedInstanceState,
                AudioListFragment.class.getName(),
                R.id.activity_main);
        fragment.setAppBarLayout(appBarLayout);
    }


}
