package ru.skyeng.listening.AudioFiles;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import io.reactivex.Observer;
import ru.skyeng.listening.AudioFiles.model.AudioData;
import ru.skyeng.listening.AudioFiles.model.AudioFile;
import ru.skyeng.listening.AudioFiles.model.AudioFilesRequestParams;
import ru.skyeng.listening.CommonComponents.Interfaces.RequestParams;
import ru.skyeng.listening.MVPBase.MVPModel;
import ru.skyeng.listening.MVPBase.MVPPresenter;
import ru.skyeng.listening.MVPBase.MVPView;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class AudioListPresenter
        extends MvpBasePresenter<MVPView<List<AudioFile>>>
        implements MVPPresenter<
        AudioData,
        List<AudioFile>,
        AudioFilesRequestParams> {

    private AudioListModel mModel;
    private Observer<AudioData> mObserver;

    @Override
    public void setObserver(Observer<AudioData> observer) {
        mObserver = observer;
    }

    @Override
    public void setModel(MVPModel<AudioData, List<AudioFile>, AudioFilesRequestParams> model) {
        this.mModel = (AudioListModel) model;
        this.mModel.initRetrofitService();
    }

    @Override
    public MVPModel<AudioData,
            List<AudioFile>,
            AudioFilesRequestParams> getModel() {
        return mModel;
    }

    @Override
    public List<AudioFile> getData() {
        return getModel().getItems();
    }

    @Override
    public void loadData(final boolean pullToRefresh, RequestParams params) {
        mModel.loadData(mObserver, (AudioFilesRequestParams) params);
    }


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

}
