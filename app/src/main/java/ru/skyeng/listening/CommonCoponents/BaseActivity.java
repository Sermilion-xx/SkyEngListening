package ru.skyeng.listening.CommonCoponents;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import ru.skyeng.listening.AudioFiles.AudioListFragment;
import ru.skyeng.listening.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    protected Toolbar setupToolbar(String title, boolean home, int... homeAsUpIndicator) {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(title);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null && home) {
            if (homeAsUpIndicator.length > 0) {
                getSupportActionBar().setHomeAsUpIndicator(homeAsUpIndicator[0]);
            }
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        return mToolbar;
    }

    protected Fragment setupRecyclerFragment(Bundle inState, String name, int containerId) {
        Fragment fragment;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (inState != null) {
            fragment = manager.getFragment(inState, name);
        } else {
            fragment = FragmentFactory.createFragmentWithName(AudioListFragment.class);
            transaction.add(containerId, fragment, name);
            transaction.commit();
        }
        return fragment;
    }
}
