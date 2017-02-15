package ru.skyeng.listening.Categories;

import android.os.Bundle;

import java.util.List;

import io.reactivex.Observer;
import ru.skyeng.listening.AudioFiles.model.AudioData;
import ru.skyeng.listening.AudioFiles.model.AudioFile;
import ru.skyeng.listening.AudioFiles.model.AudioFilesRequestParams;
import ru.skyeng.listening.Categories.model.Tag;
import ru.skyeng.listening.Categories.model.TagsData;
import ru.skyeng.listening.Categories.model.TagsRequestParams;
import ru.skyeng.listening.MVPBase.MVPModel;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 15/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class CategoriesModel implements MVPModel<TagsData, List<Tag>, TagsRequestParams> {

    @Override
    public void initRetrofitService() {

    }

    @Override
    public void loadData(Observer<TagsData> observable, TagsRequestParams params) {

    }

    @Override
    public void setData(TagsData data) {

    }

    @Override
    public List<Tag> processResult(TagsData data) {
        return null;
    }

    @Override
    public List<Tag> getItems() {
        return null;
    }

    @Override
    public Bundle getExtraData() {
        return null;
    }
}
