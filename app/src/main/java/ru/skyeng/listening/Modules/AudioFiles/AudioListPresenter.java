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

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.skyeng.listening.CommonComponents.FilterSingleton;
import ru.skyeng.listening.CommonComponents.Interfaces.MVPBase.MVPModel;
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
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.MESSAGE_PLAYBACK_TIME;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.MESSAGE_SUBTITLE_TIME;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.MESSAGE_UPDATE_ADAPTER;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.MESSAGE_UPDATE_PLAYER_UI;

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
    boolean mBound = false;
    private Messenger msgService;
    private SubtitleEngine mSubtitleEngine;
    private AudioListActivity mActivity;
    private AudioFilesRequestParams mRequestParams;

    @Inject
    public void setModel(AudioListModel model) {
        mModel = model;
        mModel.injectDependencies((SEApplication) getAppContext());
        loadData(false);
    }

    @Inject
    void setSubtitleModel(SubtitlesModel model) {
        mSubtitlesModel = model;
        mSubtitlesModel.injectDependencies((SEApplication) getAppContext());
        bindPlayerService();
    }

    @Inject
    void setSubtitleEngine(SubtitleEngine engine) {
        mSubtitleEngine = engine;
    }

    @Inject
    public void setSubtitlesModel(SubtitlesModel model) {
        this.mSubtitlesModel = model;
    }


    public AudioListPresenter() {
        mRequestParams = new AudioFilesRequestParams();

    }

    public SubtitleEngine getSubtitleEngine() {
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
    public void loadData(final boolean pullToRefresh) {
        if (mActivity == null) {
            mActivity = (AudioListActivity) getActivityContext();
        }
        SettingsObject settingsObject = FacadePreferences.getSettingsFromPref(getActivityContext());
        FilterSingleton mFilter = FilterSingleton.getInstance();
        mRequestParams.prepareDurations(mFilter.getDuration());
        if(settingsObject!=null) {
            mRequestParams.setAccentIds(new ArrayList<>(settingsObject.getAccentIds()));
            mRequestParams.setLevelId(settingsObject.getLevel());
        }
        mModel.loadData(new Observer<AudioData>() {
            @Override
            public void onSubscribe(Disposable d) {
                if (!pullToRefresh) {
                    mActivity.showProgress();
                }
            }

            @Override
            public void onNext(AudioData value) {
                if(pullToRefresh && mRequestParams.getPage()>1){
                    mModel.addData(value);
                }else {
                    if (mModel.getItems() == null || mModel.getItems().size() == 0 || pullToRefresh) {
                        mModel.setData(value);
                    } else if (mModel.getItems().size() > 0) {
                        mModel.addData(value);
                    }
                }
                mActivity.updatePlayList(mModel.getItems(), pullToRefresh);
            }

            @Override
            public void onError(Throwable e) {
                mActivity.onError(e);
            }

            @Override
            public void onComplete() {
                if (getActivityContext() != null && !pullToRefresh) {
                    mActivity.hideProgress();
                    mActivity.updateButtonsVisibility();
                }
                if (pullToRefresh)
                    mActivity.setRefreshing(false);
            }
        }, mRequestParams);
    }

    public void loadSubtitles(SubtitlesRequestParams params) {
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

    public void loadMore(int totalItemsCount) {
        if (totalItemsCount > 14) {
            mRequestParams.setPage(mRequestParams.getPage() + 1);
            loadData(true);
        }
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
                msgService.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    public void bindPlayerService() {
        if (!mBound) {
            Intent intent = new Intent(getActivityContext(), PlayerService.class);
            Messenger messenger = new Messenger(playbackHandler);
            intent.putExtra(BINDER_MESSENGER, messenger);
            getActivityContext().bindService(intent, playerConnection, Context.BIND_AUTO_CREATE);
        }
    }


    public Handler playbackHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.what == MESSAGE_PLAYBACK_TIME) {
                Bundle bundle = (Bundle) message.obj;
                mActivity.updatePlaybacktime(bundle.getLong(AUDIO_ELAPSED_TIME), bundle.getLong(AUDIO_DURATION));
            } else if (message.what == MESSAGE_SUBTITLE_TIME) {
                long time = (long) message.obj;
                mActivity.updateSubtitles(time);
            } else if (message.what == PlayerService.MESSAGE_PLAYING_FILE_STATE_FOR_COVER) {
                mActivity.onPlayerCoverClick((PlayerState) message.obj);
            } else if (message.what == MESSAGE_UPDATE_PLAYER_UI) {
                mActivity.updatePlayerUI((AudioFile) message.obj);
            } else if (message.what == MESSAGE_UPDATE_ADAPTER) {
                mActivity.updateAdapter((AudioFile) message.obj);
            }
        }
    };

    @Override
    public void injectDependencies() {
        ((SEApplication) getAppContext()).getAudioListPresenterDiComponent().inject(this);
    }

    public AudioFilesRequestParams getRequestParams() {
        return mRequestParams;
    }
}
