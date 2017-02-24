package ru.skyeng.listening.Modules.AudioFiles;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import ru.skyeng.listening.CommonComponents.FacadeCommon;
import ru.skyeng.listening.CommonComponents.PlayerCallback;
import ru.skyeng.listening.Modules.AudioFiles.model.AudioFile;
import ru.skyeng.listening.Modules.AudioFiles.player.PlayerState;
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

    private class AudioViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView mCoverImage;
        TextView mCategory;
        TextView mName;
        TextView mDescription;
        TextView mDuration;
        ImageView mPlayPause;
        View mDarkLayer;


        AudioViewHolder(View itemView) {
            super(itemView);
            mCoverImage = (RoundedImageView) itemView.findViewById(R.id.image_cover);
            mCategory = (TextView) itemView.findViewById(R.id.text_category);
            mName = (TextView) itemView.findViewById(R.id.text_name);
            mDescription = (TextView) itemView.findViewById(R.id.text_description);
            mDuration = (TextView) itemView.findViewById(R.id.text_length);
            mPlayPause = (ImageView) itemView.findViewById(R.id.audio_play_pause);
            mDarkLayer = itemView.findViewById(R.id.cover_dark_layer);
        }
    }

    private int playingPosition = -1;
    private List<AudioFile> mItems;
    private Context mContext;
    private PlayerCallback mPlayerCallback;

    public AudioListAdapter(Context context, PlayerCallback callback) {
        this.mContext = context;
        this.mPlayerCallback = callback;
    }

    public List<AudioFile> getItems() {
        return mItems;
    }

    public void setItems(List<AudioFile> mItems) {
        this.mItems = mItems;
    }

    public void setPlayingPosition(int playingPosition) {
        this.playingPosition = playingPosition;
    }

    int getPlayingPosition() {
        return playingPosition;
    }

    @Override
    public AudioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewRow = inflater.inflate(R.layout.list_item_audio_file, parent, false);
        return new AudioViewHolder(viewRow);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        AudioViewHolder holder = (AudioViewHolder) viewHolder;
        AudioFile item = getItems().get(position);
        item.setDurationInMinutes(FacadeCommon.getDateFromMillis(item.getDurationInSeconds() * 1000));
        holder.mDuration.setText(item.getDurationInMinutes());
        holder.mDescription.setText(item.getDescription());
        holder.mName.setText(item.getTitle());

        if (item.getState() == PlayerState.PLAY) {
            playingPosition = position;
            setupAudioCover(holder, R.drawable.ic_pause_blue, View.VISIBLE);
        } else if (item.getState() == PlayerState.PAUSE) {
            setupAudioCover(holder, R.drawable.ic_play_blue, View.VISIBLE);
        } else if (item.getState() == PlayerState.STOP) {
            setupAudioCover(holder, -1, View.GONE);
        }
        String category = mContext.getString(R.string.no_category);
        if (item.getTags().size() > 0) {
            category = item.getTags().get(0).get(KEY_TITLE);
        }
        holder.mCategory.setText(category);
        Glide.with(mContext)
                .load(item.getImageFileUrl())
                .asBitmap()
                .priority(Priority.HIGH)
                .centerCrop().placeholder(R.drawable.ic_player_cover)
                .into(holder.mCoverImage);
        //не делать getDrawable (потому что происходит этов новом треде - тормоз)
        holder.mCoverImage.setOnClickListener(getOnClickListener(position, holder, item));
    }

    private void setupAudioCover(AudioViewHolder viewHolder, int drawableId, int visibility) {
        if (drawableId > -1) {
            viewHolder.mPlayPause.setImageDrawable(ContextCompat.getDrawable(mContext, drawableId));
        }
        viewHolder.mPlayPause.setVisibility(visibility);
        viewHolder.mDarkLayer.setVisibility(visibility);
    }

    /**
     * Метод для установки состояния проигрования аудио файла
     * @param state: 0 - остановленно, 1 - проигрование, 2 - пауза
     */
    void setPlayerState(PlayerState state) {
        if(playingPosition<getItems().size()) {
            getItems().get(playingPosition).setState(state);
            notifyItemChanged(playingPosition);
        }
    }

    public void onSwitchAudio(AudioFile item, int position) {
        if (playingPosition < getItems().size()) {
            getItems().get(playingPosition).setState(PlayerState.STOP);
        }
        notifyItemChanged(playingPosition);
        item.setState(PlayerState.PLAY);
        notifyItemPlaying(position);
        mPlayerCallback.startPlaying(item);
    }

    public void onPlayingAudioClicked(AudioFile item, AudioViewHolder viewHolder, int position) {
        item.setState(item.getState() == PlayerState.PLAY ? PlayerState.PAUSE : PlayerState.PLAY);
        notifyItemChanged(position);
        int actionType = setPlayPauseIcon(viewHolder, item);
        if (actionType == 1) {
            mPlayerCallback.pausePlayer();
        } else if (actionType == 2) {
            mPlayerCallback.continuePlaying();
        }
    }

    public void onPlayingNewAudio(AudioFile item, int position) {
        item.setState(PlayerState.PLAY);
        notifyItemPlaying(position);
        mPlayerCallback.startPlaying(item);
    }

    @NonNull
    private View.OnClickListener getOnClickListener(int position,
                                                    AudioViewHolder viewHolder,
                                                    AudioFile item) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playingPosition != -1 && playingPosition != position) {
                    onSwitchAudio(item, position);
                } else if (playingPosition == position) {
                    onPlayingAudioClicked(item, viewHolder, position);
                } else {
                    onPlayingNewAudio(item, position);
                }
            }
        };
    }

    private void notifyItemPlaying(int position) {
        playingPosition = position;
        notifyItemChanged(position);
    }

    private int setPlayPauseIcon(AudioViewHolder viewHolder, AudioFile item) {
        int actionType = 1;
        int icon = R.drawable.ic_play_blue;
        if (item.getState() == PlayerState.PLAY) {
            icon = R.drawable.ic_pause_blue;
            actionType = 2;
        }
        viewHolder.mPlayPause.setImageDrawable(ContextCompat.getDrawable(mContext, icon));
        return actionType;
    }

    @Override
    public int getItemCount() {
        return getItems() != null ? getItems().size() : 0;
    }

}
