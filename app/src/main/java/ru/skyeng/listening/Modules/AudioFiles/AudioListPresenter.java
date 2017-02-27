package ru.skyeng.listening.Modules.AudioFiles;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.skyeng.listening.CommonComponents.FilterSingleton;
import ru.skyeng.listening.CommonComponents.Interfaces.MVPBase.MVPPresenter;
import ru.skyeng.listening.CommonComponents.Interfaces.MVPBase.MVPView;
import ru.skyeng.listening.CommonComponents.SEApplication;
import ru.skyeng.listening.Modules.AudioFiles.model.AudioData;
import ru.skyeng.listening.Modules.AudioFiles.model.AudioFile;
import ru.skyeng.listening.Modules.AudioFiles.model.AudioFilesRequestParams;
import ru.skyeng.listening.Modules.AudioFiles.model.SubtitleEngine;
import ru.skyeng.listening.Modules.AudioFiles.model.SubtitleFile;
import ru.skyeng.listening.Modules.AudioFiles.model.SubtitlesRequestParams;
import ru.skyeng.listening.Modules.AudioFiles.player.PlayerService;
import ru.skyeng.listening.Modules.AudioFiles.player.PlayerState;
import ru.skyeng.listening.Modules.Settings.model.SettingsObject;
import ru.skyeng.listening.Utility.FacadePreferences;

import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.AUDIO_DURATION;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.AUDIO_ELAPSED_TIME;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.BINDER_MESSENGER;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.CURRENT_FILE;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.EXTRA_AUDIO_URL;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.MESSAGE_PLAYBACK_TIME;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.MESSAGE_SUBTITLE_TIME;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.MESSAGE_UPDATE_ADAPTER;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.MESSAGE_UPDATE_PLAYER_UI;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.PLAYER_STATE;

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
        AudioListModel,
        List<AudioFile>,
        AudioFilesRequestParams> {

    private AudioListModel mModel;
    private SubtitlesModel mSubtitlesModel;
    private boolean mBound = false;
    private Messenger msgService;
    private SubtitleEngine mSubtitleEngine;
    private AudioFilesRequestParams mRequestParams;

    public AudioListPresenter() {
        bindPlayerService();
        mRequestParams = new AudioFilesRequestParams();
        SEApplication.getINSTANCE().getAudioListDiComponent().inject(this);
    }

    @Inject
    public void setModel(AudioListModel model) {
        mModel = model;
    }

    @Inject
    void setSubtitleModel(SubtitlesModel model) {
        mSubtitlesModel = model;
    }

    @Inject
    void setSubtitleEngine(SubtitleEngine engine) {
        mSubtitleEngine = engine;
    }

    @Inject
    public void setSubtitlesModel(SubtitlesModel model) {
        this.mSubtitlesModel = model;
    }

    SubtitleEngine getSubtitleEngine() {
        return mSubtitleEngine;
    }

    @Override
    public void clear() {
        getData().clear();
    }

    @Override
    public AudioListModel getModel() {
        return mModel;
    }

    @Override
    public List<AudioFile> getData() {
        return getModel().getItems();
    }

    @Override
    public void loadData(final boolean pullToRefresh) {
        SettingsObject settingsObject = FacadePreferences.getSettingsFromPref(getActivityContext());
        FilterSingleton mFilter = FilterSingleton.getInstance();
        mRequestParams.prepareDurations(mFilter.getDuration());
        if (settingsObject != null) {
            mRequestParams.setAccentIds(new ArrayList<>(settingsObject.getAccentIds()));
            mRequestParams.setLevelId(settingsObject.getLevel());
        }
        Observable<AudioData> audioDataObservable = mModel.loadData(mRequestParams);
        audioDataObservable.subscribe(new Observer<AudioData>() {
            @Override
            public void onSubscribe(Disposable d) {
                if (!pullToRefresh) {
                    showProgress();
                }
            }

            @Override
            public void onNext(AudioData value) {
                if (pullToRefresh && mRequestParams.getPage() > 1) {
                    mModel.addData(value);
                } else {
                    if (mModel.getItems() == null || mModel.getItems().size() == 0 || pullToRefresh) {
                        mModel.setData(value);
                    } else if (mModel.getItems().size() > 0) {
                        mModel.addData(value);
                    }
                }
                if (getActivityContext() != null) {
                    getActivityContext().updatePlayList(mModel.getItems());
                }
            }

            @Override
            public void onError(Throwable e) {
                if (getActivityContext() != null) {
                    getActivityContext().onError(e);
                }
            }

            @Override
            public void onComplete() {
                if (getActivityContext() != null && !pullToRefresh) {
                    getActivityContext().hideProgress();
                    getActivityContext().updateButtonsVisibility();
                }
                if (pullToRefresh && getActivityContext() != null)
                    getActivityContext().setRefreshing(false);
            }
        });
    }

    private void showProgress() {
        if (getActivityContext() != null)
            getActivityContext().showProgress();
    }

    void loadAudioAndSubtitles(AudioFile audioFile){
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_AUDIO_URL, audioFile.getAudioFileUrl());
        sendMessage(bundle, PlayerService.MESSAGE_START_BUFFERING, audioFile);
        loadSubtitles(new SubtitlesRequestParams(audioFile.getId()));
    }

    public void loadDataIfEmpty(){
        if(getData()==null){
            loadData(false);
        }
    }

    private void loadSubtitles(SubtitlesRequestParams params) {
        Observable<List<SubtitleFile>> subtitlesDataObservable = mSubtitlesModel.loadData(params);
        subtitlesDataObservable.subscribe(new Observer<List<SubtitleFile>>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<SubtitleFile> value) {
                mSubtitleEngine.setSubtitles(value);
                sendMessage(null, PlayerService.MESSAGE_PLAY);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    void loadMore(int totalItemsCount) {
        if (totalItemsCount > 14) {
            mRequestParams.setPage(mRequestParams.getPage() + 1);
            loadData(true);
        }
    }

    @Override
    public Context getAppContext() {
        return SEApplication.getINSTANCE();
    }

    @Override
    public AudioListActivity getActivityContext() {
        if (getView() != null) {
            return (AudioListActivity) getView().getActivityContext();
        }
        return null;
    }

    private ServiceConnection playerConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder binder) {
            mBound = true;
            msgService = new Messenger(binder);
        }

        public void onServiceDisconnected(ComponentName className) {
            mBound = false;
        }
    };

    void sendMessage(Bundle bundle, int type, Object... obj) {
        if (mBound) {
            try {
                if (msgService != null) {
                    Message message = Message.obtain(null, type, 1, 1);
                    if (obj.length != 0) {
                        message.obj = obj[0];
                    }
                    message.setData(bundle);
                    msgService.send(message);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void bindPlayerService() {
        if (!mBound) {
            Intent intent = new Intent(getAppContext(), PlayerService.class);
            Messenger messenger = new Messenger(playbackHandler);
            intent.putExtra(BINDER_MESSENGER, messenger);
            getAppContext().bindService(intent, playerConnection, Context.BIND_AUTO_CREATE);
            mBound = true;
        }
    }

    private Handler playbackHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.what == MESSAGE_PLAYBACK_TIME) {
                Bundle bundle = (Bundle) message.obj;
                getActivityContext().updatePlaybackTime(bundle.getLong(AUDIO_ELAPSED_TIME), bundle.getLong(AUDIO_DURATION));
            } else if (message.what == MESSAGE_SUBTITLE_TIME) {
                long time = (long) message.obj;
                getActivityContext().updateSubtitles(time);
            } else if (message.what == PlayerService.MESSAGE_PLAYING_FILE_STATE_FOR_COVER) {
                getActivityContext().onPlayerCoverClick((PlayerState) message.obj);
            } else if (message.what == MESSAGE_UPDATE_PLAYER_UI) {
                Bundle bundle = (Bundle) message.obj;
                getActivityContext().updatePlayerUI(bundle.getParcelable(CURRENT_FILE), (PlayerState) bundle.getSerializable(PLAYER_STATE), false);
            } else if (message.what == MESSAGE_UPDATE_ADAPTER) {
                Bundle bundle = (Bundle) message.obj;
                getActivityContext().updateAdapter(bundle.getParcelable(CURRENT_FILE));
            }
        }
    };

    AudioFilesRequestParams getRequestParams() {
        return mRequestParams;
    }

    void onStop() {
        if (mBound) {
            getAppContext().unbindService(playerConnection);
            mBound = false;
        }
    }

    void onStart(){
        bindPlayerService();
    }

}
