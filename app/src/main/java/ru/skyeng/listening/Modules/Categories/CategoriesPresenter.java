package ru.skyeng.listening.Modules.Categories;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import io.reactivex.Observer;
import ru.skyeng.listening.Modules.Categories.model.CategoriesRequestParams;
import ru.skyeng.listening.MVPBase.MVPModel;
import ru.skyeng.listening.MVPBase.MVPPresenter;
import ru.skyeng.listening.MVPBase.MVPView;
import ru.skyeng.listening.Modules.Categories.model.AudioTag;
import ru.skyeng.listening.Modules.Categories.model.TagsData;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 15/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class CategoriesPresenter extends MvpBasePresenter<MVPView>
        implements MVPPresenter<
        TagsData,
        List<AudioTag>,
        CategoriesRequestParams> {

    private CategoriesModel mModel;
    private Observer<TagsData> mObserver;

    @Override
    public Context getAppContext() {
        if (getView() != null) {
            return getView().getAppContext();
        }
        return null;
    }

    @Override
    public Context getActivityContext() {
        if (getView() != null) {
            return getView().getActivityContext();
        }
        return null;
    }

    @Override
    public void setModel(MVPModel<TagsData, List<AudioTag>, CategoriesRequestParams> model) {
        this.mModel = (CategoriesModel) model;
    }

    @Override
    public void clear() {
        getData().clear();
    }

    @Override
    public MVPModel<TagsData, List<AudioTag>, CategoriesRequestParams> getModel() {
        return mModel;
    }

    @Override
    public List<AudioTag> getData() {
        return getModel().getItems();
    }

    @Override
    public void loadData(boolean pullToRefresh, CategoriesRequestParams params) {
        mModel.loadData(mObserver,  params);
    }
}
