package ru.skyeng.listening.AudioFiles;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import ru.skyeng.listening.R;

public class AudioListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar("Listening", false);
        setupRecyclerFragment(savedInstanceState, AudioListFragment.class.getName(), R.id.activity_main);
    }

    protected AudioListFragment setupRecyclerFragment(Bundle inState, String name, int containerId) {
        AudioListFragment fragment;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (inState != null) {
            fragment = (AudioListFragment) manager.getFragment(inState, name);
        } else {
            fragment = new AudioListFragment();
            transaction.add(containerId, fragment, name);
            transaction.commit();
        }
        return fragment;
    }

    protected void setupToolbar(String title, boolean home) {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(title);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null && home) {
            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

}
