package ru.skyeng.listening.Categories;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import ru.skyeng.listening.AudioFiles.AudioListFragment;
import ru.skyeng.listening.AudioFiles.model.AudioData;
import ru.skyeng.listening.AudioFiles.model.AudioFile;
import ru.skyeng.listening.AudioFiles.model.AudioFilesRequestParams;
import ru.skyeng.listening.Categories.model.Tag;
import ru.skyeng.listening.Categories.model.TagsData;
import ru.skyeng.listening.Categories.model.TagsRequestParams;
import ru.skyeng.listening.MVPBase.MVPPresenter;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 15/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class CategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private MVPPresenter<TagsData, List<Tag>, TagsRequestParams> mPresenter;
    private CategoriesFragment mFragment;

    private List<Tag> getItems() {
        return mPresenter.getModel().getItems();
    }

    void setPresenter(MVPPresenter<TagsData, List<Tag>, TagsRequestParams> mPresenter) {
        this.mPresenter = mPresenter;
    }

    void setFragment(CategoriesFragment mFragment) {
        this.mFragment = mFragment;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
