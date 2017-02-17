package ru.skyeng.listening.Modules.AudioFiles.player;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;

import java.lang.ref.WeakReference;

import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.EXTRA_AUDIO_URL;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.MESSAGE_CONTINUE;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.MESSAGE_PAUSE;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.MESSAGE_PLAY;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.MESSAGE_PLAYBACK_SEARCH;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.MESSAGE_PLAYBACK_TIME;

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
    private Messenger replyMessenger;

    public Messenger getReplyMessenger() {
        return replyMessenger;
    }

    public IncomingHandler(PlayerService service){
        mService = service;
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == MESSAGE_PLAY) {
            Bundle bundle = msg.getData();
            mService.getPlayer().setPlaySource(bundle.getString(EXTRA_AUDIO_URL));
            mService.getPlayer().play();
            mService.startSendingPlaybackTime();
        } else if (msg.what == MESSAGE_PAUSE) {
            mService.getPlayer().pause();
            mService.stopSendingPlaybackTime();
        } else if (msg.what == MESSAGE_CONTINUE) {
            mService.getPlayer().play();
            mService.startSendingPlaybackTime();
        } else if(msg.what == MESSAGE_PLAYBACK_SEARCH){
            mService.getPlayer().getPlayer().seekTo((long)msg.obj);
        }
    }
}
