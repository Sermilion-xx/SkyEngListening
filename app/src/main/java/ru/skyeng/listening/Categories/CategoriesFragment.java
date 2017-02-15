package ru.skyeng.listening.Categories;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.skyeng.listening.Categories.model.AudioTag;
import ru.skyeng.listening.Categories.model.TagsData;
import ru.skyeng.listening.Categories.model.TagsRequestParams;
import ru.skyeng.listening.CommonComponents.Interfaces.ActivityExtensions;
import ru.skyeng.listening.CommonComponents.SEApplication;
import ru.skyeng.listening.MVPBase.MVPView;
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

public class CategoriesFragment extends MvpLceFragment<
        SwipeRefreshLayout,
        List<AudioTag>,
        MVPView<List<AudioTag>>,
        CategoriesPresenter>
        implements MVPView<List<AudioTag>>,
        SwipeRefreshLayout.OnRefreshListener, Observer<TagsData> {

    private List<Integer> mSelectedIds;
    protected boolean isRefreshing;
    @Inject
    CategoriesAdapter mAdapter;
    @BindView(R.id.tag_group)
    TagView tagGroup;

    private List<Integer> selectedTags;


    public void addSelectedId(Integer id){
        mSelectedIds.add(id);
    }

    public void removeSelectedId(Integer id){
        mSelectedIds.remove(id);
    }

    @Inject
    public void setPresenter(@NonNull CategoriesPresenter presenter) {
        presenter.setObserver(this);
        super.setPresenter(presenter);
    }

    @Inject
    void setModel(CategoriesModel model) {
        presenter.setModel(model);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mSelectedIds = new ArrayList<>();
        ((SEApplication) getAppContext()).getCategoriesDiComponent().inject(this);
        selectedTags = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_list, container, false);
    }

    @NonNull
    @Override
    public CategoriesPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);
        ButterKnife.bind(this, view);
        contentView.setOnRefreshListener(this);
        tagGroup.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
                if(selectedTags.contains(position)){
                    selectedTags.remove(Integer.valueOf(position));
                }else {
                    selectedTags.add(position);
                }
                initTagView(selectedTags);
            }
        });


        if ((presenter.getModel()).getItems() == null) {
            loadData(false);
        }else {
            initTagView(selectedTags);
        }
    }


    @Override
    public void showLoading(boolean pullToRefresh) {
        super.showLoading(pullToRefresh);
        contentView.setRefreshing(pullToRefresh);
    }


    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        if (e != null)
            return e.getMessage();
        return getActivityContext().getString(R.string.unknown_error);
    }

    @Override
    public void setData(List<AudioTag> data) {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        if (!pullToRefresh)
            ((ActivityExtensions) getActivity()).showProgress();
        presenter.loadData(pullToRefresh, new TagsRequestParams());
    }



    @Override
    public void onRefresh() {
        isRefreshing = true;
        loadData(true);
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(TagsData value) {
        presenter.getModel().setData(value);
        isRefreshing = false;
        setData(value.getPrimaryData());
        initTagView(selectedTags);
    }

    private void initTagView(List<Integer> selected) {
        tagGroup.removeAll();
        for(int i=0; i<presenter.getData().size(); i++){
            AudioTag aTag = presenter.getData().get(i);
            Tag tag = new Tag(aTag.getTitle());
            tag.radius = 6;
            if(selected.contains(i)){
                tag.background = ContextCompat.getDrawable(getActivityContext(), R.drawable.blue2_with_shadow);
                tag.tagTextColor = ContextCompat.getColor(getActivityContext(), R.color.colorWhite);
            }else {
                tag.layoutColor = ContextCompat.getColor(getActivityContext(), R.color.colorBlue1);
                tag.tagTextColor = ContextCompat.getColor(getActivityContext(), R.color.colorBlue2);
            }
            tag.tagTextSize = 16;
            tagGroup.addTag(tag);
        }
    }

    @Override
    public void onError(Throwable e) {
        isRefreshing = false;
        showError(e.getCause(), isRefreshing);
        contentView.setRefreshing(false);
        e.printStackTrace();
        setData(new ArrayList<>());
    }

    @Override
    public void onComplete() {
        ((ActivityExtensions) getActivity()).hideProgress();
        contentView.setRefreshing(isRefreshing);
    }

    @Override
    public Context getAppContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }
}
