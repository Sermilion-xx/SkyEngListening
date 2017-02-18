package ru.skyeng.listening.CommonComponents;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import ru.skyeng.listening.CommonComponents.Interfaces.ActivityExtensions;
import ru.skyeng.listening.R;

public class BaseActivity extends AppCompatActivity implements ActivityExtensions {

    protected ProgressBar mProgress;
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    protected Toolbar setupToolbar(String title, int... homeAsUpIndicator) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(title);
        setSupportActionBar(mToolbar);
        if (homeAsUpIndicator.length > 0) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setHomeAsUpIndicator(homeAsUpIndicator[0]);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
        }
        return mToolbar;
    }

    protected Toolbar setupToolbar(String title, Drawable drawable) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(title);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(drawable);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        return mToolbar;
    }

    protected Fragment setupRecyclerFragment(Bundle inState, Class<? extends Fragment> fragmentClass, int containerId) {
        Fragment fragment;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (inState != null) {
            fragment = manager.getFragment(inState, fragmentClass.getName());
        } else {
            fragment = FragmentFactory.createFragmentWithName(fragmentClass);
            transaction.add(containerId, fragment, fragmentClass.getName());
            transaction.commit();
        }
        return fragment;
    }

    public void showProgress() {
        mProgress.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
