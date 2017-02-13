package ru.skyeng.listening.AudioFiles;

import android.media.MediaPlayer;

import java.io.IOException;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 13/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class AudioPlayer {

    private boolean isPaused;
    private MediaPlayer mMediaPlayer;
    private String extraData;

    public AudioPlayer(){
        mMediaPlayer = new MediaPlayer();
    }

    public void setDataSource(String url) throws IOException {
        mMediaPlayer.setDataSource(url);
    }

    public String getExtraData() {
        return extraData;
    }

    public void playAudio(String url) throws Exception {
        killMediaPlayer();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setDataSource(url);
        mMediaPlayer.prepare();
        mMediaPlayer.start();
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public boolean isNull(){
        return mMediaPlayer==null;
    }

    public void playPause(){
        try {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            } else {
                mMediaPlayer.start();
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public void killMediaPlayer() {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void release() {
        mMediaPlayer.release();
    }
}
