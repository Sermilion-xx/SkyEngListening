package ru.skyeng.listening.AudioFiles;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.skyeng.listening.CommonCoponents.BaseActivity;
import ru.skyeng.listening.R;

public class AudioListActivity extends BaseActivity {

    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        initCollapsingToolbar();
        setupToolbar(getString(R.string.Listening), false);
        AudioListFragment fragment = (AudioListFragment) setupRecyclerFragment(savedInstanceState,
                AudioListFragment.class.getName(),
                R.id.fragment_container);
        if(fragment!=null){
            fragment.setAppBarLayout(appBarLayout);
        }
    }

//    private void initCollapsingToolbar() {
//        final CollapsingToolbarLayout collapsingToolbar =
//                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbar.setTitle(" ");
//        appBarLayout.setExpanded(true);
//        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            boolean isShow = false;
//            int scrollRange = -1;
//
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (scrollRange == -1) {
//                    scrollRange = appBarLayout.getTotalScrollRange();
//                }
//                if (scrollRange + verticalOffset == 0) {
//                    collapsingToolbar.setTitle(getString(R.string.app_name));
//                    isShow = true;
//                } else if (isShow) {
//                    collapsingToolbar.setTitle(" ");
//                    isShow = false;
//                }
//            }
//        });
//    }


}
