package ru.skyeng.listening.AudioFiles;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.skyeng.listening.AudioFiles.domain.AudioData;
import ru.skyeng.listening.AudioFiles.domain.AudioFile;
import ru.skyeng.listening.AudioFiles.domain.AudioFilesRequestParams;
import ru.skyeng.listening.CommonCoponents.RequestParams;
import ru.skyeng.listening.CommonCoponents.SECallback;
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

    @Override
    public void setModel(MVPModel<AudioData, List<AudioFile>, AudioFilesRequestParams> model) {
        this.mModel = (AudioListModel) model;
        this.mModel.initRetrofitService();
    }

    @Override
    public MVPModel getModel() {
        return mModel;
    }

    @Override
    public void loadData(final boolean pullToRefresh,
                         RequestParams params) {
        mModel.loadData(new Observer<AudioData>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(AudioData value) {
                if (isViewAttached()) {
                    getView().setData(value.getAudioFiles());
                    getView().showContent();
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().showError(e.getCause(), pullToRefresh);
                }
            }

            @Override
            public void onComplete() {

            }
        }, (AudioFilesRequestParams) params);
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
