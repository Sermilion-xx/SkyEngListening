package ru.skyeng.listening.AudioFiles;

import android.os.Bundle;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
    private AudioData mData;

    @Override
    public void loadData(final SECallback<List<AudioFile>> callback,
                         AudioFilesRequestParams params) {
        ServiceGenerator serviceGenerator = new ServiceGenerator();
        AudioFilesService audioFilesService = serviceGenerator.createService(AudioFilesService.class);
        Observable<AudioData> call = audioFilesService.getAudioFiles(
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
                call.subscribe(audioData -> {
                    mData = audioData;
                    callback.onSuccess(mData.getAudioFiles());
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
