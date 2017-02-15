package ru.skyeng.listening.Categories;

import android.os.Bundle;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.skyeng.listening.Categories.model.AudioTag;
import ru.skyeng.listening.Categories.model.TagsData;
import ru.skyeng.listening.Categories.model.TagsRequestParams;
import ru.skyeng.listening.Categories.network.TagsService;
import ru.skyeng.listening.CommonComponents.ServiceGenerator;
import ru.skyeng.listening.MVPBase.MVPModel;

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

public class CategoriesModel implements MVPModel<TagsData, List<AudioTag>, TagsRequestParams> {

    private TagsService mTagsService;
    private TagsData mData;

    @Override
    public void initRetrofitService() {
        ServiceGenerator serviceGenerator = new ServiceGenerator();
        mTagsService = serviceGenerator.createService(TagsService.class);
    }

    @Override
    public void loadData(Observer<TagsData> observable, TagsRequestParams params) {
        if(params==null)
            params = new TagsRequestParams();
        Observable<TagsData> audioDataObservable = mTagsService.getTags(
                params.getPage(),
                params.getPageSize())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        audioDataObservable.subscribe(observable);
    }

    @Override
    public void setData(TagsData data) {
        this.mData = data;
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
