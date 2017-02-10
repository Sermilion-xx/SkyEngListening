package ru.skyeng.listening.AudioFiles;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.skyeng.listening.AudioFiles.Network.AudioFilesService;
import ru.skyeng.listening.AudioFiles.domain.AudioData;
import ru.skyeng.listening.AudioFiles.domain.AudioFile;
import ru.skyeng.listening.AudioFiles.domain.AudioFilesRequestParams;
import ru.skyeng.listening.CommonCoponents.SECallback;
import ru.skyeng.listening.MVPBase.MVPModel;
import ru.skyeng.listening.ServiceGenerator;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */
public class AudioFilesModel implements MVPModel<AudioData, List<AudioFile>, AudioFilesRequestParams> {

    private static final String CURRENT_PAGE = "currentPage";
    private static final String LAST_PAGE = "lastPage";
    private AudioData mData;

    @Override
    public void loadData(final SECallback<List<AudioFile>> callback,
                         AudioFilesRequestParams params) {
        ServiceGenerator serviceGenerator = new ServiceGenerator();
        AudioFilesService audioFilesService = serviceGenerator.createService(AudioFilesService.class);
        Call<AudioData> call = audioFilesService.getAudioFiles(
                params.getPage(),
                params.getPageSize(),
                params.getTitle(),
                params.getAccentId(),
                params.getLevelId(),
                params.getTagIds(),
                params.getDurationGT(),
                params.getDurationLT());
        call.enqueue(new Callback<AudioData>() {
            @Override
            public void onResponse(Call<AudioData> call, Response<AudioData> response) {
                mData = response.body();
                callback.onSuccess(mData.getAudioFiles());
            }

            @Override
            public void onFailure(Call<AudioData> call, Throwable t) {
                callback.onError(t);
            }
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
