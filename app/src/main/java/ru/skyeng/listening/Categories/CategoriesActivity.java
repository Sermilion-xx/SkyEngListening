package ru.skyeng.listening.Categories;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.skyeng.listening.CommonComponents.BaseActivity;
import ru.skyeng.listening.R;

public class CategoriesActivity extends BaseActivity{

    private static final String TAG_CATEGORIES_FRAGMENT = CategoriesFragment.class.getName();
    private CategoriesFragment mFragment;

    @BindView(R.id.button_reset)
    Button mResetTagsButton;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mFragment != null) {
            getSupportFragmentManager().putFragment(outState, TAG_CATEGORIES_FRAGMENT, mFragment);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        ButterKnife.bind(this);
        setupToolbar(getString(R.string.select_categories), true, R.drawable.ic_x);
        mFragment = (CategoriesFragment) setupRecyclerFragment(
                savedInstanceState,
                CategoriesFragment.class,
                R.id.fragment_container
        );
        mResetTagsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragment.resetSelectedTags();
            }
        });
    }

}
