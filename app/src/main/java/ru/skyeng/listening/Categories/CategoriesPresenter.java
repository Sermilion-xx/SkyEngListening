package ru.skyeng.listening.Categories;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import io.reactivex.Observer;
import ru.skyeng.listening.Categories.model.AudioTag;
import ru.skyeng.listening.Categories.model.TagsData;
import ru.skyeng.listening.Categories.model.TagsRequestParams;
import ru.skyeng.listening.CommonComponents.Interfaces.RequestParams;
import ru.skyeng.listening.MVPBase.MVPModel;
import ru.skyeng.listening.MVPBase.MVPPresenter;
import ru.skyeng.listening.MVPBase.MVPView;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 15/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class CategoriesPresenter extends MvpBasePresenter<MVPView<List<AudioTag>>>
        implements MVPPresenter<
        TagsData,
        List<AudioTag>,
        TagsRequestParams> {

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
    public void setModel(MVPModel<TagsData, List<AudioTag>, TagsRequestParams> model) {
        this.mModel = (CategoriesModel) model;
        this.mModel.initRetrofitService();
    }

    @Override
    public MVPModel<TagsData, List<AudioTag>, TagsRequestParams> getModel() {
        return mModel;
    }

    @Override
    public List<AudioTag> getData() {
        return getModel().getItems();
    }

    @Override
    public void loadData(boolean pullToRefresh, RequestParams params) {
        mModel.loadData(mObserver, (TagsRequestParams) params);
    }

    @Override
    public void setObserver(Observer<TagsData> observer) {
        mObserver = observer;
    }
}
