package ru.skyeng.listening.Modules.AudioFiles.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Map;

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

    private final int id;
    private final String title;
    private final String description;
    private final String audioFileUrl;
    private final String imageFileUrl;
    private final int wordsInMinute;
    private final int durationInSeconds;
    private final String durationInMinutes;
    private List<Map<String, String>> tags;


    protected AudioFile(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        audioFileUrl = in.readString();
        imageFileUrl = in.readString();
        wordsInMinute = in.readInt();
        durationInSeconds = in.readInt();
        durationInMinutes = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(title);
        out.writeString(description);
        out.writeString(audioFileUrl);
        out.writeString(imageFileUrl);
        out.writeInt(wordsInMinute);
        out.writeInt(durationInSeconds);
        out.writeString(durationInMinutes);
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

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public List<Map<String, String>> getTags() {
        return tags;
    }

    public void setTags(List<Map<String, String>> tags) {
        this.tags = tags;
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
