package ru.skyeng.listening.Modules.AudioFiles.player;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import ru.skyeng.listening.CommonComponents.FacadeCommon;
import ru.skyeng.listening.Modules.AudioFiles.SubtitleEngine;
import ru.skyeng.listening.Modules.AudioFiles.model.AudioFile;
import ru.skyeng.listening.Modules.AudioFiles.model.SubtitleFile;
import ru.skyeng.listening.R;

import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.MESSAGE_PLAYBACK_TIME;
import static ru.skyeng.listening.Modules.AudioFiles.player.PlayerService.MESSAGE_SUBTITLE_TIME;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 18/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class PlayerHandler extends Handler {

    private SeekBar   audioSeek;
    private TextView  audioPlayed;
    private AudioFile mAudioFile;
    private TextView  audioLeft;
    private Context   mContext;
    private SubtitleEngine mSubtitleEngine;
    private TextView  audioSubtitles;

    public PlayerHandler(SeekBar audioSeek,
                         TextView audioPlayed,
                                 AudioFile mAudioFile,
                                 TextView audioLeft,
                                 Context mContext,
                                 SubtitleEngine mSubtitleEngine,
                                 TextView audioSubtitles){
        this.audioSeek = audioSeek;
        this.audioPlayed = audioPlayed;
        this.mAudioFile = mAudioFile;
        this.audioLeft = audioLeft;
        this.mContext = mContext;
        this.mSubtitleEngine = mSubtitleEngine;
        this.audioSubtitles = audioSubtitles;

    }

    @Override
    public void handleMessage(Message message) {
        super.handleMessage(message);
        if (message.what == MESSAGE_PLAYBACK_TIME) {
            long time = (long) message.obj / 1000;
            audioSeek.setProgress((int) time);
            audioPlayed.setText(FacadeCommon.getDateFromMillis((Long) message.obj));
            long duration = mAudioFile.getDurationInSeconds();
            audioLeft.setText(String.format(mContext.getString(R.string.leftTime), FacadeCommon.getDateFromMillis(duration * 1000 - (long) message.obj)));
        } else if (message.what == MESSAGE_SUBTITLE_TIME) {
            long time = (long) message.obj * 1000;
            if (mSubtitleEngine.size() > 0)
                audioSubtitles.setText(mSubtitleEngine.updateSubtitles(time).getTextEn());
        }
    }
}
