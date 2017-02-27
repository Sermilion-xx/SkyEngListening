package ru.skyeng.listening.Modules.AudioFiles.player;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.skyeng.listening.CommonComponents.FacadeCommon;
import ru.skyeng.listening.R;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 27/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class PlayerFragment extends Fragment {

    @BindView(R.id.audio_cover)
    RoundedImageView audioCoverImage;
    @BindView(R.id.audio_title)
    TextView audioTitle;
    @BindView(R.id.audio_left)
    TextView audioLeft;
    @BindView(R.id.audio_seek)
    SeekBar audioSeek;
    @BindView(R.id.audio_played)
    TextView audioPlayed;
    @BindView(R.id.audio_subtitles)
    TextView audioSubtitles;
    @BindView(R.id.audio_play_pause)
    ImageView audioPlayPause;
    @BindView(R.id.cover_dark_layer)
    View mDarkLayer;

    PlayerFragmentCallback mPlayerFragmentCallback;

    public void setPlayerCallback(PlayerFragmentCallback mPlayerFragmentCallback) {
        this.mPlayerFragmentCallback = mPlayerFragmentCallback;
    }

    public static PlayerFragment getInstance(PlayerFragmentCallback mPlayerFragmentCallback){
        PlayerFragment fragment = new PlayerFragment();
        fragment.setPlayerCallback(mPlayerFragmentCallback);
        return fragment;
    }

    public RoundedImageView getAudioCoverImage() {
        return audioCoverImage;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        ButterKnife.bind(this, view);
        audioCoverImage.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
        audioCoverImage.setImageResource(R.drawable.ic_player_cover);
        audioSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int currentProgress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPlayerFragmentCallback.seekTo(currentProgress);
            }
        });

        audioCoverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayerFragmentCallback.updateCover();
            }
        });
        return view;
    }
    public void hideCoverDarkMask(){
        mDarkLayer.setVisibility(View.GONE);
    }

    public void showCoverDarkMask(){
        mDarkLayer.setVisibility(View.GONE);
    }

    public void setPlayPauseImage(int resource){
        audioPlayPause.setImageResource(resource);
    }

    public void hidePlayPauseView(){
        audioPlayPause.setVisibility(View.GONE);
    }

    public void showPlayPauseView(){
        audioPlayPause.setVisibility(View.VISIBLE);
    }

    public void setTitleColor(int color){
        audioTitle.setTextColor(ContextCompat.getColor(getActivity(), color));
    }

    public void setAudioTitle(String title){
        audioTitle.setText(title);
    }

    public void setAudioDuration(int duration){
        audioSeek.setMax(duration);
    }

    public void setSubtitles(String text){
        audioSubtitles.setText(text);
    }

    public void updatePlaybackInfo(long elapsedTime, long duration){
        audioSeek.setProgress((int) elapsedTime / 1000);
        audioPlayed.setText(FacadeCommon.getDateFromMillis(elapsedTime));
        audioLeft.setText(String.format(getString(R.string.leftTime), FacadeCommon.getDateFromMillis(duration * 1000 - elapsedTime)));
    }

}
