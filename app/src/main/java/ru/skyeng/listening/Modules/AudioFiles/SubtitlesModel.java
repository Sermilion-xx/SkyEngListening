package ru.skyeng.listening.Modules.AudioFiles;

import android.os.Bundle;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.skyeng.listening.MVPBase.MVPModel;
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
    private SubtitlesRequestParams mParams;

    public SubtitlesModel(){
        mParams = new SubtitlesRequestParams();
    }

    public void setRetrofitService(SubtitlesService service) {
        mSubtitlesService = service;
    }

    @Override
    public void loadData(Observer<List<SubtitleFile>> observable, SubtitlesRequestParams params) {
        Observable<List<SubtitleFile>> subtitlesDataObservable = mSubtitlesService.getSubtitles(
                params.getAudioId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        subtitlesDataObservable.subscribe(observable);
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
        if(mData==null) return null;
        return mData;
    }

    @Override
    public Bundle getExtraData() {
        return null;
    }

    @Override
    public SubtitlesRequestParams getRequestParams() {
        return null;
    }
}
