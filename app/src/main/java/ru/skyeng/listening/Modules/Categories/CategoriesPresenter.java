package ru.skyeng.listening.Modules.Categories;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.skyeng.listening.CommonComponents.Interfaces.ActivityExtensions;
import ru.skyeng.listening.CommonComponents.Interfaces.MVPBase.MVPPresenter;
import ru.skyeng.listening.CommonComponents.Interfaces.MVPBase.MVPView;
import ru.skyeng.listening.CommonComponents.SEApplication;
import ru.skyeng.listening.Modules.Categories.model.AudioTag;
import ru.skyeng.listening.Modules.Categories.model.CategoriesRequestParams;
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
        CategoriesModel,
        List<AudioTag>,
        CategoriesRequestParams> {

    private CategoriesModel mModel;
    private CategoriesRequestParams mRequestParams;

    public CategoriesPresenter(){
        this.mRequestParams = new CategoriesRequestParams();
        SEApplication.getINSTANCE().getCategoriesPresenterDiComponent().inject(this);
    }

    @Override
    @Inject
    public void setModel(CategoriesModel model) {
        this.mModel = model;
    }

    @Override
    public CategoriesModel getModel() {
        return mModel;
    }

    @Override
    public void clear() {
        getData().clear();
    }


    @Override
    public List<AudioTag> getData() {
        return getModel().getItems();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        Observable<TagsData> tagsDataObservable = mModel.loadData(mRequestParams);
        tagsDataObservable.subscribe(new Observer<TagsData>() {
            @Override
            public void onSubscribe(Disposable d) {
                getActivityContext().showProgress();
            }

            @Override
            public void onNext(TagsData value) {
                getModel().setData(value);
                if (getActivityContext() != null) {
                    getActivityContext().initTagView();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                if (getView() != null) {
                    getActivityContext().hideProgress();
                }

            }
        });
    }

    @Override
    public Context getAppContext() {
        if (getView() != null) {
            return getView().getAppContext();
        }
        return null;
    }

    @Override
    public CategoriesActivity getActivityContext() {
        if (getView() != null) {
            return (CategoriesActivity) getView().getActivityContext();
        }
        return null;
    }

}
