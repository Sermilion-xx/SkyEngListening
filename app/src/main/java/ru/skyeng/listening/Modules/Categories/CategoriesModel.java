package ru.skyeng.listening.Modules.Categories;

import android.os.Bundle;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.skyeng.listening.CommonComponents.BaseRequestParams;
import ru.skyeng.listening.MVPBase.MVPModel;
import ru.skyeng.listening.Modules.Categories.model.AudioTag;
import ru.skyeng.listening.Modules.Categories.model.TagsData;
import ru.skyeng.listening.Modules.Categories.network.TagsService;

import static ru.skyeng.listening.CommonComponents.Constants.CURRENT_PAGE;
import static ru.skyeng.listening.CommonComponents.Constants.LAST_PAGE;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 15/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class CategoriesModel implements MVPModel<TagsData, List<AudioTag>, BaseRequestParams> {

    private TagsService mTagsService;
    private TagsData mData;

    public void setRetrofitService(TagsService service) {
        mTagsService = service;
    }

    @Override
    public void loadData(Observer<TagsData> observable, BaseRequestParams params) {
        if(params==null)
            params = new BaseRequestParams();
        Observable<TagsData> tagsDataObservable = mTagsService.getTags(
                params.getPage(),
                params.getPageSize())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        tagsDataObservable.subscribe(observable);
    }

    @Override
    public void setData(TagsData data) {
        this.mData = data;
    }

    @Override
    public void addData(TagsData data) {

    }

    @Override
    public List<AudioTag> processResult(TagsData data) {
        return data.getPrimaryData();
    }

    @Override
    public List<AudioTag> getItems() {
        if(mData==null) return null;
        return mData.getPrimaryData();
    }

    @Override
    public Bundle getExtraData() {
        Bundle bundle = new Bundle();
        bundle.putInt(CURRENT_PAGE, Integer.parseInt(mData.getMetaData().get(CURRENT_PAGE)));
        bundle.putInt(LAST_PAGE, Integer.parseInt(mData.getMetaData().get(LAST_PAGE)));
        return bundle;
    }
}
