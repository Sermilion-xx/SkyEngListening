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
import ru.skyeng.listening.CommonComponents.PlayerAdapterCallback;
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
    private PlayerAdapterCallback mPlayerCallback;
    private PlayerState mPlayerState;

    public AudioListAdapter(Context context, PlayerAdapterCallback callback) {
        this.mContext = context;
        this.mPlayerCallback = callback;
        this.mPlayerState = PlayerState.STOP;
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
        holder.mDuration.setText(FacadeCommon.getDateFromMillis(item.getDurationInSeconds() * 1000));
        holder.mDescription.setText(item.getDescription());
        holder.mName.setText(item.getTitle());

        if (mPlayerState == PlayerState.PLAY && playingPosition == position) {
            setupAudioCover(holder, R.drawable.ic_pause_blue, View.VISIBLE);
        } else if (mPlayerState == PlayerState.PAUSE && playingPosition == position) {
            setupAudioCover(holder, R.drawable.ic_play_blue, View.VISIBLE);
        } else if (playingPosition!= position) {
            setupAudioCover(holder, -1, View.GONE);
        }

        String category = mContext.getString(R.string.no_category);
        if(item.getTags().get(0)!=null){
            category = item.getTags().get(0).get(KEY_TITLE);
        }
        holder.mCategory.setText(category);
        Glide.with(mContext)
                .load(item.getImageFileUrl())
                .asBitmap()
                .priority(Priority.HIGH)
                .centerCrop().placeholder(R.drawable.ic_player_cover)
                .into(holder.mCoverImage);
        holder.mCoverImage.setOnClickListener(getOnClickListener(position, holder, item));
    }


    private void setupAudioCover(AudioViewHolder viewHolder, int drawableId, int visibility) {
        if (drawableId > -1) {
            viewHolder.mPlayPause.setImageResource(drawableId);
        }
        viewHolder.mPlayPause.setVisibility(visibility);
        viewHolder.mDarkLayer.setVisibility(visibility);
    }

    public void onSwitchAudio(AudioFile item, int position) {
        mPlayerState = PlayerState.STOP;
        notifyItemChanged(playingPosition);
        playingPosition = position;
        mPlayerCallback.onPlay(item);
    }

    public void onPlayingAudioClicked(AudioViewHolder viewHolder, int position) {
        PlayerState actionType = setPlayPauseIcon(viewHolder);
        if (actionType == PlayerState.PAUSE) {
            mPlayerState = PlayerState.PLAY;
            mPlayerCallback.onPause();
        } else if (actionType == PlayerState.PLAY) {
            mPlayerState = PlayerState.PAUSE;
            mPlayerCallback.onContinue();
        }
        notifyItemChanged(position);
    }

    public void onPlayingNewAudio(AudioFile item, int position) {
        playingPosition = position;
        mPlayerCallback.onPlay(item);
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
                    onPlayingAudioClicked(viewHolder, position);
                } else {
                    onPlayingNewAudio(item, position);
                }
            }
        };
    }

    public void notifyPlayerStateChanged(PlayerState playerState) {
        this.mPlayerState = playerState;
        notifyItemChanged(playingPosition);
    }

    private PlayerState setPlayPauseIcon(AudioViewHolder viewHolder) {
        PlayerState actionType = PlayerState.PLAY;
        int icon = R.drawable.ic_play_blue;
        if (mPlayerState == PlayerState.PLAY) {
            icon = R.drawable.ic_pause_blue;
            actionType = PlayerState.PAUSE;
        }
        viewHolder.mPlayPause.setImageDrawable(ContextCompat.getDrawable(mContext, icon));
        return actionType;
    }

    @Override
    public int getItemCount() {
        return getItems() != null ? getItems().size() : 0;
    }

}
