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
    private Bitmap imageBitmap;
    private int wordsInMinute;
    private Map<String, String> accent;
    private Map<String, String> level;
    private List<Map<String, String>> tags;
    private int durationInSeconds;
    private String durationInMinutes;
    private PlayerState state;
    private boolean loading;
    private List<SubtitleFile> mSubtitles;

    public AudioFile() {
        state = PlayerState.STOP;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public void setSubtitles(List<SubtitleFile> mSubtitles) {
        this.mSubtitles = mSubtitles;
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
        imageBitmap = in.readParcelable(Bitmap.class.getClassLoader());
        wordsInMinute = in.readInt();
        durationInSeconds = in.readInt();
        durationInMinutes = in.readString();
        int stateInt = in.readInt();
        state = PlayerState.values()[stateInt];
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

    public void setDurationInMinutes(String durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAudioFileUrl() {
        return audioFileUrl;
    }

    public void setAudioFileUrl(String audioFileUrl) {
        this.audioFileUrl = audioFileUrl;
    }

    public String getImageFileUrl() {
        return imageFileUrl;
    }

    public void setImageFileUrl(String imageFileUrl) {
        this.imageFileUrl = imageFileUrl;
    }

    public int getWordsInMinute() {
        return wordsInMinute;
    }

    public void setWordsInMinute(int wordsInMinute) {
        this.wordsInMinute = wordsInMinute;
    }

    public Map<String, String> getAccent() {
        return accent;
    }

    public void setAccent(Map<String, String> accent) {
        this.accent = accent;
    }

    public Map<String, String> getLevel() {
        return level;
    }

    public void setLevel(Map<String, String> level) {
        this.level = level;
    }

    public List<Map<String, String>> getTags() {
        return tags;
    }

    public void setTags(List<Map<String, String>> tags) {
        this.tags = tags;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
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
        dest.writeParcelable(imageBitmap, flags);
        dest.writeInt(wordsInMinute);
        dest.writeInt(durationInSeconds);
        dest.writeString(durationInMinutes);
        int stateTmp = state == null ? -1 : state.ordinal();
        dest.writeInt(stateTmp);
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
