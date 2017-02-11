package ru.skyeng.listening.AudioFiles;

import android.os.Bundle;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.skyeng.listening.AudioFiles.domain.AudioData;
import ru.skyeng.listening.AudioFiles.domain.AudioFile;
import ru.skyeng.listening.AudioFiles.domain.AudioFilesRequestParams;
import ru.skyeng.listening.AudioFiles.network.AudioFilesService;
import ru.skyeng.listening.CommonCoponents.SECallback;
import ru.skyeng.listening.CommonCoponents.ServiceGenerator;
import ru.skyeng.listening.MVPBase.MVPModel;

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
    private static final String LAST_PAGE = "lastPage";
    private AudioFilesService audioFilesService;
    private AudioData mData;

    @Override
    public void initRetrofitService() {
        ServiceGenerator serviceGenerator = new ServiceGenerator();
        audioFilesService = serviceGenerator.createService(AudioFilesService.class);
    }

    @Override
    public void loadData(Observer<AudioData> observable, AudioFilesRequestParams params) {
        Observable<AudioData> audioDataObservable = audioFilesService.getAudioFiles(
                params.getPage(),
                params.getPageSize(),
                params.getTitle(),
                params.getAccentId(),
                params.getLevelId(),
                params.getTagIds(),
                params.getDurationGT(),
                params.getDurationLT())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        audioDataObservable.subscribe(data->{
            mData = data;
            observable.onNext(data);
        });
    }


    @Override
    public List<AudioFile> processResult(AudioData data) {
        return data.getAudioFiles();
    }

    @Override
    public List<AudioFile> getItems() {
        return mData.getAudioFiles();
    }

    @Override
    public Bundle getExtraData() {
        Bundle bundle = new Bundle();
        bundle.putInt(CURRENT_PAGE, Integer.parseInt(mData.getMeta().get(CURRENT_PAGE)));
        bundle.putInt(LAST_PAGE, Integer.parseInt(mData.getMeta().get(LAST_PAGE)));
        return bundle;
    }

}
