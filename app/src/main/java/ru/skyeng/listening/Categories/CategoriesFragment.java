package ru.skyeng.listening.Categories;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.skyeng.listening.Categories.model.Tag;
import ru.skyeng.listening.Categories.model.TagsData;
import ru.skyeng.listening.CommonComponents.BaseFragment;
import ru.skyeng.listening.CommonComponents.SEApplication;
import ru.skyeng.listening.R;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 15/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class CategoriesFragment extends BaseFragment<Tag, CategoriesPresenter, CategoriesAdapter, TagsData>  {

    private List<Integer> mSelectedIds;

    public void addSelectedId(Integer id){
        mSelectedIds.add(id);
    }

    public void removeSelectedId(Integer id){
        mSelectedIds.remove(id);
    }

    @Override
    @Inject
    public void setPresenter(@NonNull CategoriesPresenter presenter) {
        presenter.setObserver(this);
        super.setPresenter(presenter);
    }

    @Inject
    void setModel(CategoriesModel model) {
        presenter.setModel(model);
    }

    @Inject
    public void setAdapter(CategoriesAdapter adapter){
        mAdapter = adapter;
    }

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mSelectedIds = new ArrayList<>();
        ((SEApplication) getAppContext()).getCategoriesDiComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);
        ButterKnife.bind(this, view);
        contentView.setOnRefreshListener(this);
        mAdapter.setPresenter(presenter);
        mAdapter.setFragment(this);
        mRecyclerView.setAdapter(mAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        int viewWidth = mRecyclerView.getMeasuredWidth();
                        float cardViewWidth = getActivity().getResources().getDimension(R.dimen.cardview_layout_width);
                        int newSpanCount = (int) Math.floor(viewWidth / cardViewWidth);
                        layoutManager.setSpanCount(newSpanCount);
                        layoutManager.requestLayout();
                    }
                });
        if ((presenter.getModel()).getItems() == null) {
            loadData(false);
        }
    }
}
