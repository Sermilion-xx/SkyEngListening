package ru.skyeng.listening.Modules.AudioFiles;

import android.os.Bundle;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.skyeng.listening.CommonComponents.Interfaces.MVPBase.MVPModel;
import ru.skyeng.listening.CommonComponents.SEApplication;
import ru.skyeng.listening.Modules.AudioFiles.model.SubtitleFile;
import ru.skyeng.listening.Modules.AudioFiles.model.SubtitlesRequestParams;
import ru.skyeng.listening.Modules.AudioFiles.network.SubtitlesService;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 17/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class SubtitlesModel implements MVPModel<List<SubtitleFile>,
        List<SubtitleFile>,
        SubtitlesRequestParams> {

    private SubtitlesService mSubtitlesService;
    private List<SubtitleFile> mData;

    public SubtitlesModel(){
        SEApplication.getINSTANCE().getAudioListDiComponent().inject(this);
    }

    @Inject
    void setSubtitlesService(SubtitlesService service) {
        mSubtitlesService = service;
    }

    @Override
    public Observable<List<SubtitleFile>> loadData(SubtitlesRequestParams params) {
        return mSubtitlesService.getSubtitles(
                params.getAudioId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void setData(List<SubtitleFile> data) {
        this.mData = data;
    }

    @Override
    public void addData(List<SubtitleFile> data) {

    }

    @Override
    public List<SubtitleFile> processResult(List<SubtitleFile> data) {
        return data;
    }

    @Override
    public List<SubtitleFile> getItems() {
        if (mData == null) return null;
        return mData;
    }

    @Override
    public Bundle getExtraData() {
        return null;
    }

}
