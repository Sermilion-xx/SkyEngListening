package ru.skyeng.listening.Modules.AudioFiles;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.skyeng.listening.Modules.AudioFiles.model.AudioData;
import ru.skyeng.listening.Modules.AudioFiles.model.AudioFile;
import ru.skyeng.listening.Modules.AudioFiles.model.AudioFilesRequestParams;
import ru.skyeng.listening.Modules.AudioFiles.network.AudioFilesService;
import ru.skyeng.listening.CommonComponents.ServiceGenerator;
import ru.skyeng.listening.MVPBase.MVPModel;
import ru.skyeng.listening.Modules.Settings.model.SettingsObject;
import ru.skyeng.listening.Utility.FacadePreferences;

import static ru.skyeng.listening.CommonComponents.Constants.CURRENT_PAGE;
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


    private AudioFilesService audioFilesService;
    private AudioData mData;

    AudioData getAudioData(){
        return mData;
    }

    @Override
    public void initRetrofitService() {
        ServiceGenerator serviceGenerator = new ServiceGenerator();
        audioFilesService = serviceGenerator.createService(AudioFilesService.class);
    }

    @Override
    public void loadData(Observer<AudioData> observable, AudioFilesRequestParams params) {
        if(params==null)
            params = new AudioFilesRequestParams();
        SettingsObject settingsObject = FacadePreferences.getSettingsFromPref(((AudioListFragment)observable).getActivityContext());
        if(settingsObject!=null) {
            params.setAccentIds(new ArrayList<>(settingsObject.getAccentIds()));
            params.setLevelId(settingsObject.getLevel());
            params.setDuration(settingsObject.getDuration());
        }
        Observable<AudioData> audioDataObservable = audioFilesService.getAudioFiles(
                params.getPage(),
                params.getPageSize(),
                params.getTitle(),
                params.getAccentIds(),
                params.getLevelId(),
                params.getTagIds(),
                params.getDurationGT(),
                params.getDurationLT(),
                params.getDuration())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        audioDataObservable.subscribe(observable);
    }

    @Override
    public void setData(AudioData data) {
        this.mData = data;
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
        bundle.putInt(CURRENT_PAGE, Integer.parseInt(mData.getMetaData().get(CURRENT_PAGE)));
        bundle.putInt(LAST_PAGE, Integer.parseInt(mData.getMetaData().get(LAST_PAGE)));
        return bundle;
    }

}
