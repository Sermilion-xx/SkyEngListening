package ru.skyeng.listening.Categories;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.skyeng.listening.CommonComponents.BaseActivity;
import ru.skyeng.listening.R;

import static android.R.attr.cacheColorHint;
import static android.R.attr.data;
import static android.R.attr.switchMinWidth;
import static ru.skyeng.listening.AudioFiles.AudioListActivity.TAG_REQUEST_DATA;

public class CategoriesActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG_CATEGORIES_FRAGMENT = CategoriesFragment.class.getName();
    private CategoriesFragment mFragment;

    @BindView(R.id.button_reset)
    Button mResetTagsButton;
    @BindView(R.id.button_apply)
    Button mApplyTagsButton;

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
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>(){}.getType();
        mFragment.setSelectedTags(gson.fromJson(getIntent().getStringExtra(TAG_REQUEST_DATA), type));
        mApplyTagsButton.setOnClickListener(this);
        mResetTagsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_apply:
                applyCategories();
                break;
            case R.id.button_reset:
                mFragment.resetSelectedTags();
                setResult(Activity.RESULT_OK, new Intent());
                finish();
                break;
        }
    }

    private void applyCategories() {
        if(mFragment.getSelectedTags().size()>0){
            Intent returnIntent = new Intent();
            Gson gson = new Gson();
            String jsonTags = gson.toJson(mFragment.getSelectedTags());
            returnIntent.putExtra(TAG_REQUEST_DATA, jsonTags);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else {
            Toast.makeText(CategoriesActivity.this, getResources().getString(R.string.select_tags), Toast.LENGTH_LONG).show();
        }
    }
}
