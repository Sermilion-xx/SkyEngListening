package ru.skyeng.listening.Modules.AudioFiles;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.skyeng.listening.Modules.AudioFiles.model.AudioData;
import ru.skyeng.listening.Modules.AudioFiles.model.AudioFile;
import ru.skyeng.listening.Modules.AudioFiles.model.AudioFilesRequestParams;
import ru.skyeng.listening.MVPBase.MVPModel;
import ru.skyeng.listening.MVPBase.MVPPresenter;
import ru.skyeng.listening.MVPBase.MVPView;
import ru.skyeng.listening.Modules.AudioFiles.model.SubtitleEngine;
import ru.skyeng.listening.Modules.AudioFiles.model.SubtitleFile;
import ru.skyeng.listening.Modules.AudioFiles.model.SubtitlesRequestParams;
import ru.skyeng.listening.Modules.Settings.model.SettingsObject;
import ru.skyeng.listening.Utility.FacadePreferences;

import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.MESSAGE_PLAYBACK_TIME;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.MESSAGE_SUBTITLE_TIME;

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
        extends MvpBasePresenter<MVPView>
        implements MVPPresenter<
        AudioData,
        List<AudioFile>,
        AudioFilesRequestParams> {

    private AudioListModel mModel;
    private SubtitlesModel mSubtitlesModel;
    boolean mBound = false;
    private Messenger msgService;
    private SubtitleEngine mSubtitleEngine;
    private AudioListActivity mActivity;



    public AudioListPresenter() {
        mSubtitleEngine = new SubtitleEngine();

    }

    public SubtitleEngine getSubtitleEngine() {
        return mSubtitleEngine;
    }

    @Override
    public void setModel(MVPModel<AudioData, List<AudioFile>, AudioFilesRequestParams> model) {
        this.mModel = (AudioListModel) model;
    }

    @Override
    public void clear() {
        getData().clear();
    }

    public void setSubtitlesModel(MVPModel<List<SubtitleFile>, List<SubtitleFile>, SubtitlesRequestParams> model) {
        this.mSubtitlesModel = (SubtitlesModel) model;
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
    public void loadData(final boolean pullToRefresh, AudioFilesRequestParams params) {
        if(mActivity== null) {
            mActivity = (AudioListActivity) getActivityContext();
        }
        if(params==null) {
            params = new AudioFilesRequestParams();
        }
        SettingsObject settingsObject = FacadePreferences.getSettingsFromPref(getActivityContext());
        if(settingsObject!=null) {
            params.setAccentIds(new ArrayList<>(settingsObject.getAccentIds()));
            params.setLevelId(settingsObject.getLevel());
            List<Integer> durationValues = settingsObject.getDuration();
            Map<String, Integer> paramsMap = new HashMap<>();
            int valueIndex = 0;
            for(int i = 0; i<durationValues.size()/2; i++) {
                for(int j = 0; j < 2; j ++) {
                    paramsMap.put("durations["+i+"]["+j+"]", durationValues.get(valueIndex));
                    valueIndex ++;
                }
            }
            params.setDuration(paramsMap);
        }

        mModel.loadData(new Observer<AudioData>() {

            @Override
            public void onSubscribe(Disposable d) {
                mActivity.showProgress();
            }

            @Override
            public void onNext(AudioData value) {
                if (mModel.getItems() == null || mModel.getItems().size() == 0) {
                    mModel.setData(value);
                } else if (mModel.getItems().size() > 0) {
                    mModel.addData(value);
                }
                mActivity.onNext(value);
            }

            @Override
            public void onError(Throwable e) {
                mActivity.onError(e);
            }

            @Override
            public void onComplete() {
                if (getActivityContext() != null) {
                    mActivity.hideProgress();
                    mActivity.updateButtonsVisibility();
                }
                if (pullToRefresh)
                    mActivity.setRefreshing(false);
            }
        }, params);
    }

    public void loadSubtitles(SubtitlesRequestParams params){
        mSubtitlesModel.loadData(new Observer<List<SubtitleFile>>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<SubtitleFile> value) {
                mSubtitleEngine.setSubtitles(value);
                mActivity.startPlayerMessage();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        }, params);
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

    public ServiceConnection playerConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder binder) {
            mBound = true;
            msgService = new Messenger(binder);
        }
        public void onServiceDisconnected(ComponentName className) {
            mBound = false;
        }
    };

    //Binding к PlayerService
    public void sendMessage(Bundle bundle, int type, Object... obj) {
        if (mBound) {
            try {
                Message message = Message.obtain(null, type, 1, 1);
                if (obj.length != 0) {
                    message.obj = obj[0];
                }
                message.setData(bundle);
                msgService.send(message); //sending message to service
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public Handler playbackHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.what == MESSAGE_PLAYBACK_TIME) {
                mActivity.handlePlaybackTimeMessage(message);
            } else if (message.what == MESSAGE_SUBTITLE_TIME) {
                mActivity.handleSubtitleMessage(message);
            }
        }
    };



}
