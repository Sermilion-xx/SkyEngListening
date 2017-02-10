package ru.skyeng.listening.AudioFiles;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ru.skyeng.listening.R;

public class AudioListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupRecyclerFragment(savedInstanceState, AudioListFragment.class.getName(),R.id.activity_main);
    }

    protected AudioListFragment setupRecyclerFragment(Bundle inState, String name, int containerId) {
        AudioListFragment fragment;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (inState != null) {
            fragment = (AudioListFragment) manager.getFragment(inState, name);
        } else {
            fragment = new AudioListFragment();
            transaction.add(containerId, fragment , name);
            transaction.commit();
        }
        return fragment;
    }
}
