package ru.skyeng.listening.Modules.AudioFiles.model;

import android.graphics.Bitmap;
import android.media.session.PlaybackState;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Map;

import ru.skyeng.listening.Modules.AudioFiles.player.PlayerState;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class AudioFile implements Parcelable, Comparable<AudioFile>{

    private int id;
    private String title;
    private String description;
    private String audioFileUrl;
    private String imageFileUrl;
    private int wordsInMinute;
    private Map<String, String> accent;
    private Map<String, String> level;
    private List<Map<String, String>> tags;
    private int durationInSeconds;
    private String durationInMinutes;
    private boolean loading;
    private List<SubtitleFile> mSubtitles;

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }
    public List<SubtitleFile> getSubtitles() {
        return mSubtitles;
    }

    protected AudioFile(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        audioFileUrl = in.readString();
        imageFileUrl = in.readString();
        wordsInMinute = in.readInt();
        durationInSeconds = in.readInt();
        durationInMinutes = in.readString();
        int stateInt = in.readInt();
    }

    public static final Creator<AudioFile> CREATOR = new Creator<AudioFile>() {
        @Override
        public AudioFile createFromParcel(Parcel in) {
            return new AudioFile(in);
        }

        @Override
        public AudioFile[] newArray(int size) {
            return new AudioFile[size];
        }
    };

    public String getDurationInMinutes() {
        return durationInMinutes;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAudioFileUrl() {
        return audioFileUrl;
    }

    public String getImageFileUrl() {
        return imageFileUrl;
    }

    public int getWordsInMinute() {
        return wordsInMinute;
    }

    public Map<String, String> getAccent() {
        return accent;
    }

    public Map<String, String> getLevel() {
        return level;
    }

    public List<Map<String, String>> getTags() {
        return tags;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(audioFileUrl);
        dest.writeString(imageFileUrl);
        dest.writeInt(wordsInMinute);
        dest.writeInt(durationInSeconds);
        dest.writeString(durationInMinutes);
    }


    @Override
    public int compareTo(AudioFile o) {
        if(o==null) return -1;
        if(this.getAudioFileUrl().equals(o.getAudioFileUrl())){
            return 0;
        }else {
            return -1;
        }
    }
}
