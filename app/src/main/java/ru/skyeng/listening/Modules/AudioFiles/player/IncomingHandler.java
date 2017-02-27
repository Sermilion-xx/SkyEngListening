package ru.skyeng.listening.Modules.AudioFiles.player;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import ru.skyeng.listening.Modules.AudioFiles.model.AudioFile;

import static ru.skyeng.listening.Modules.AudioFiles.AudioListActivity.ACTION_UPDATE_ADAPTER;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.MESSAGE_CONTINUE;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.MESSAGE_PLAYING_FILE_STATE_FOR_COVER;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.MESSAGE_PAUSE;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.MESSAGE_PLAY;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.MESSAGE_PLAYBACK_SEARCH;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.MESSAGE_START_BUFFERING;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.MESSAGE_UPDATE_ADAPTER;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.MESSAGE_UPDATE_PLAYER_UI;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 17/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class IncomingHandler extends Handler {

    private PlayerService mService;

    public IncomingHandler(PlayerService service) {
        mService = service;
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == MESSAGE_PLAY) {
            mService.getPlayer().setState(PlayerState.PLAY);
            mService.stopSendingPlaybackTime();
            mService.getPlayer().play();
            mService.startSendingPlaybackTime();
            mService.sendPlayingAudioFile(MESSAGE_UPDATE_PLAYER_UI);
        } else if (msg.what == MESSAGE_PAUSE) {
            mService.getPlayer().pause();
            mService.stopSendingPlaybackTime();
            mService.sendPlayingAudioFile(MESSAGE_UPDATE_PLAYER_UI);
        } else if (msg.what == MESSAGE_CONTINUE) {
            mService.getPlayer().setState(PlayerState.PLAY);
            mService.getPlayer().play();
            mService.startSendingPlaybackTime();
            mService.sendPlayingAudioFile(MESSAGE_UPDATE_PLAYER_UI);
        } else if (msg.what == MESSAGE_PLAYBACK_SEARCH) {
            mService.getPlayer().getPlayer().seekTo((long) msg.obj);
        } else if (msg.what == MESSAGE_START_BUFFERING) {
            Bundle bundle = msg.getData();
            mService.setPlayerSource(bundle);
            mService.getPlayer().setAudioFile((AudioFile) msg.obj);
        } else if(msg.what == MESSAGE_PLAYING_FILE_STATE_FOR_COVER){
            mService.sendPlayerState();
        } else if (msg.what == MESSAGE_UPDATE_PLAYER_UI) {
                mService.sendPlayingAudioFile(MESSAGE_UPDATE_PLAYER_UI);
        } else if (msg.what == MESSAGE_UPDATE_ADAPTER){
            mService.sendPlayingAudioFile(MESSAGE_UPDATE_ADAPTER);
        }
    }
}
