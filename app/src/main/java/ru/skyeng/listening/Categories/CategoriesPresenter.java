package ru.skyeng.listening.Categories;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import io.reactivex.Observer;
import ru.skyeng.listening.AudioFiles.model.AudioData;
import ru.skyeng.listening.AudioFiles.model.AudioFile;
import ru.skyeng.listening.AudioFiles.model.AudioFilesRequestParams;
import ru.skyeng.listening.Categories.model.Tag;
import ru.skyeng.listening.Categories.model.TagsData;
import ru.skyeng.listening.Categories.model.TagsRequestParams;
import ru.skyeng.listening.CommonComponents.RequestParams;
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

public class CategoriesPresenter extends MvpBasePresenter<MVPView<List<Tag>>>
        implements MVPPresenter<
        TagsData,
        List<Tag>,
        TagsRequestParams> {

    @Override
    public Context getAppContext() {
        return null;
    }

    @Override
    public Context getActivityContext() {
        return null;
    }

    @Override
    public void setModel(MVPModel<TagsData, List<Tag>, TagsRequestParams> models) {

    }

    @Override
    public MVPModel<TagsData, List<Tag>, TagsRequestParams> getModel() {
        return null;
    }

    @Override
    public List<Tag> getData() {
        return null;
    }

    @Override
    public void loadData(boolean pullToRefresh, RequestParams params) {

    }

    @Override
    public void setObserver(Observer<TagsData> observer) {

    }
}
