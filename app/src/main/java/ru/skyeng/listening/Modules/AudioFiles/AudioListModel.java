package ru.skyeng.listening.Modules.AudioFiles;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.skyeng.listening.CommonComponents.Constants;
import ru.skyeng.listening.Modules.AudioFiles.model.AudioData;
import ru.skyeng.listening.Modules.AudioFiles.model.AudioFile;
import ru.skyeng.listening.Modules.AudioFiles.model.AudioFilesRequestParams;
import ru.skyeng.listening.Modules.AudioFiles.model.SubtitlesRequestParams;
import ru.skyeng.listening.Modules.AudioFiles.network.AudioFilesService;
import ru.skyeng.listening.MVPBase.MVPModel;
import ru.skyeng.listening.Modules.Settings.model.SettingsObject;
import ru.skyeng.listening.Utility.FacadePreferences;

import static ru.skyeng.listening.CommonComponents.Constants.LAST_PAGE;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class AudioListModel implements MVPModel<AudioData, List<AudioFile>, AudioFilesRequestParams> {

    private static final String CURRENT_PAGE = "currentPage";
    private AudioFilesService audioFilesService;
    private AudioData mData;
    private AudioFilesRequestParams mRequestParams;

    public AudioListModel(){
        mRequestParams = new AudioFilesRequestParams();
    }


    public void setRetrofitService(AudioFilesService service) {
        audioFilesService = service;
    }

    @Override
    public void loadData(Observer<AudioData> observable, AudioFilesRequestParams params) {
        Observable<AudioData> audioDataObservable = audioFilesService.getAudioFiles(
                params.getDuration(),
                params.getPage(),
                params.getPageSize(),
                params.getTitle(),
                params.getAccentIds(),
                params.getLevelId(),
                params.getTagIds(),
                params.getDurationGT(),
                params.getDurationLT()
                ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        audioDataObservable.subscribe(observable);
    }

    @Override
    public void setData(AudioData data) {
        this.mData = data;
    }

    @Override
    public void addData(AudioData data) {
        mData.getPrimaryData().addAll(data.getPrimaryData());
        mData.getMetaData().put(CURRENT_PAGE, data.getMetaData().get(CURRENT_PAGE));
    }

    @Override
    public List<AudioFile> processResult(AudioData data) {
        return data.getPrimaryData();
    }

    @Override
    public List<AudioFile> getItems() {
        if(mData!=null){
            return mData.getPrimaryData();
        }
        return null;
    }

    @Override
    public Bundle getExtraData() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.CURRENT_PAGE, Integer.parseInt(mData.getMetaData().get(Constants.CURRENT_PAGE)));
        bundle.putInt(LAST_PAGE, Integer.parseInt(mData.getMetaData().get(LAST_PAGE)));
        return bundle;
    }

    @Override
    public AudioFilesRequestParams getRequestParams() {
        return mRequestParams;
    }

}
