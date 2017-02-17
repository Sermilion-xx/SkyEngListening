package ru.skyeng.listening.Modules.AudioFiles;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import io.reactivex.Observer;
import ru.skyeng.listening.Modules.AudioFiles.model.AudioData;
import ru.skyeng.listening.Modules.AudioFiles.model.AudioFile;
import ru.skyeng.listening.Modules.AudioFiles.model.AudioFilesRequestParams;
import ru.skyeng.listening.CommonComponents.Interfaces.RequestParams;
import ru.skyeng.listening.MVPBase.MVPModel;
import ru.skyeng.listening.MVPBase.MVPPresenter;
import ru.skyeng.listening.MVPBase.MVPView;
import ru.skyeng.listening.Modules.AudioFiles.model.SubtitleFile;
import ru.skyeng.listening.Modules.AudioFiles.model.SubtitlesData;
import ru.skyeng.listening.Modules.AudioFiles.model.SubtitlesRequestParams;

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
    private SubtitlesModel mSubtitlesModel;
    private Observer<AudioData> mObserver;
    private Observer<List<SubtitleFile>> mSubtitlesObserver;

    @Override
    public void setObserver(Observer<AudioData> observer) {
        mObserver = observer;
    }

    public void setSubtitlesObserver(Observer<List<SubtitleFile>> observer) {
        mSubtitlesObserver = observer;
    }

    @Override
    public void setModel(MVPModel<AudioData, List<AudioFile>, AudioFilesRequestParams> model) {
        this.mModel = (AudioListModel) model;
        this.mModel.initRetrofitService();
    }

    public void setSubtitlesModel(MVPModel<List<SubtitleFile>, List<SubtitleFile>, SubtitlesRequestParams> model) {
        this.mSubtitlesModel = (SubtitlesModel) model;
        this.mSubtitlesModel.initRetrofitService();
    }

    @Override
    public MVPModel<AudioData,
            List<AudioFile>,
            AudioFilesRequestParams> getModel() {
        return mModel;
    }

    public MVPModel<List<SubtitleFile>,
            List<SubtitleFile>,
            SubtitlesRequestParams> getSubtitlesModel() {
        return mSubtitlesModel;
    }

    @Override
    public List<AudioFile> getData() {
        return getModel().getItems();
    }

    public List<SubtitleFile> getSubtitlesData() {
        return getSubtitlesModel().getItems();
    }



    @Override
    public void loadData(final boolean pullToRefresh, RequestParams params) {
        mModel.loadData(mObserver, (AudioFilesRequestParams) params);
    }

    public void loadSubtitles(SubtitlesRequestParams params){
        mSubtitlesModel.loadData(mSubtitlesObserver, params);
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
