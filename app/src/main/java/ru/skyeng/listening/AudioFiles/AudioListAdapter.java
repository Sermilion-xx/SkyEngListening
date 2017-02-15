package ru.skyeng.listening.AudioFiles;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ru.skyeng.listening.AudioFiles.model.AudioData;
import ru.skyeng.listening.AudioFiles.model.AudioFile;
import ru.skyeng.listening.AudioFiles.model.AudioFilesRequestParams;
import ru.skyeng.listening.CommonComponents.FacadeCommon;
import ru.skyeng.listening.MVPBase.MVPPresenter;
import ru.skyeng.listening.R;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class AudioListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String KEY_TITLE = "title";
    private MVPPresenter<AudioData, List<AudioFile>, AudioFilesRequestParams> mPresenter;
    private AudioListFragment mFragment;
    private int playingPosition = -1;

    private List<AudioFile> getItems() {
        return mPresenter.getModel().getItems();
    }

    int getPlayingPosition() {
        return playingPosition;
    }

    void setPresenter(MVPPresenter<AudioData, List<AudioFile>, AudioFilesRequestParams> mPresenter) {
        this.mPresenter = mPresenter;
    }

    private Context getContext() {
        return mPresenter.getActivityContext();
    }

    void setFragment(AudioListFragment mFragment) {
        this.mFragment = mFragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewRow = inflater.inflate(R.layout.list_item_audio_file, parent, false);
        return new AudioViewHolder(viewRow);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AudioViewHolder viewHolder = (AudioViewHolder) holder;
        AudioFile item = getItems().get(position);
        item.setDurationInMinutes(FacadeCommon.getDateFromMillis(item.getDurationInSeconds() * 1000));
        viewHolder.mDuration.setText(item.getDurationInMinutes());
        viewHolder.mDescription.setText(item.getDescription());
        viewHolder.mName.setText(item.getTitle());

        if (item.getState() == 1) {
            playingPosition = position;
            setupAudioCover(viewHolder, R.drawable.ic_pause_blue, View.VISIBLE);
        } else if (item.getState() == 2) {
            setupAudioCover(viewHolder, R.drawable.ic_play_blue, View.VISIBLE);
        } else if (item.getState() == 0) {
            setupAudioCover(viewHolder, -1, View.GONE);
        }

        String category = getContext().getString(R.string.no_category);
        if (item.getTags().size() > 0) {
            category = item.getTags().get(0).get(KEY_TITLE);
        }
        viewHolder.mCategory.setText(category);

        if (item.getImageFileUrl() != null && item.getImageBitmap() == null) {
            Glide.with(getContext())
                    .load(item.getImageFileUrl())
                    .into(viewHolder.mCoverImage);
            item.setImageBitmap(((BitmapDrawable) viewHolder.mCoverImage.getDrawable()).getBitmap());
        } else {
            if (item.getImageBitmap() != null) {
                viewHolder.mCoverImage.setImageBitmap(item.getImageBitmap());
            }
        }
        viewHolder.mCoverImage.setOnClickListener(getOnClickListener(position, viewHolder, item));
    }

    private void setupAudioCover(AudioViewHolder viewHolder, int drawableId, int visibility) {
        if (drawableId > -1) {
            viewHolder.mPlayPause.setImageDrawable(ContextCompat.getDrawable(getContext(), drawableId));
        }
        viewHolder.mPlayPause.setVisibility(visibility);
        viewHolder.mDarkLayer.setVisibility(visibility);
    }

    /**
     * Метод для установки состояния проигрования аудио файла
     * @param state: 0 - остановленно, 1 - проигрование, 2 - пауза
     */
    void setPlayerState(int state) {
        if (state < 0 || state > 2)
            throw new IllegalArgumentException("State должен быть одним из 0 - остановленно, 1 - проигрование, 2 - пауза");
        getItems().get(playingPosition).setState(state);
        notifyItemChanged(playingPosition);
    }

    @NonNull
    private View.OnClickListener getOnClickListener(int position,
                                                    AudioViewHolder viewHolder,
                                                    AudioFile item) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //playing different audio
                if (playingPosition != -1 && playingPosition != position) {
                    getItems().get(playingPosition).setState(0);
                    notifyItemChanged(playingPosition);
                    item.setState(1);
                    showItemPlaying(position, item);
                } else if (playingPosition == position) { //currently playing audio clicked
                    //0 - stopped, 1 - playing, 2 - paused
                    item.setState(item.getState() == 1 ? 2 : 1);
                    notifyItemChanged(position);
                    int actionType = setPlayPauseIcon(viewHolder, item);
                    if (actionType == 1) {
                        mFragment.pausePlayer(R.drawable.ic_play_white);
                    } else if (actionType == 0) {
                        mFragment.startPlaying(item);
                    }
                } else { // playing new audio
                    item.setState(1);
                    showItemPlaying(position, item);
                }
            }
        };

    }

    private void showItemPlaying(int position, AudioFile item) {
        playingPosition = position;
        mFragment.startPlaying(item);
        notifyItemChanged(position);
    }

    private int setPlayPauseIcon(AudioViewHolder viewHolder, AudioFile item) {
        int actionType = 1;
        int icon = R.drawable.ic_play_blue;
        if (item.getState() == 1) {
            icon = R.drawable.ic_pause_blue;
            actionType = 0;
        }
        viewHolder.mPlayPause.setImageDrawable(ContextCompat.getDrawable(getContext(), icon));
        return actionType;
    }


    @Override
    public int getItemCount() {
        return getItems() != null ? getItems().size() : 0;
    }

    private class AudioViewHolder extends RecyclerView.ViewHolder {

        ImageView mCoverImage;
        TextView mCategory;
        TextView mName;
        TextView mDescription;
        TextView mDuration;
        ImageView mPlayPause;
        View mDarkLayer;


        AudioViewHolder(View itemView) {
            super(itemView);
            mCoverImage = (ImageView) itemView.findViewById(R.id.image_cover);
            mCategory = (TextView) itemView.findViewById(R.id.text_category);
            mName = (TextView) itemView.findViewById(R.id.text_name);
            mDescription = (TextView) itemView.findViewById(R.id.text_description);
            mDuration = (TextView) itemView.findViewById(R.id.text_length);
            mPlayPause = (ImageView) itemView.findViewById(R.id.audio_play_pause);
            mDarkLayer = itemView.findViewById(R.id.cover_dark_layer);
        }
    }
}
