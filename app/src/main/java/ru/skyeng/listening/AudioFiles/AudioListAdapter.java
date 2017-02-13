package ru.skyeng.listening.AudioFiles;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ru.skyeng.listening.AudioFiles.domain.AudioFile;
import ru.skyeng.listening.CommonCoponents.FacadeCommon;
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
    private List<AudioFile> mItems;
    private Context mContext;
    private AudioListFragment mFragment;
    private int playingPosition = -1;

    void setContext(Context mContext) {
        this.mContext = mContext;
    }

    void setFragment(AudioListFragment mFragment) {
        this.mFragment = mFragment;
    }

    public AudioListAdapter() {
        mItems = new ArrayList<>();
    }

    void setAudioFiles(List<AudioFile> data) {
        mItems = data;
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
        AudioFile item = mItems.get(position);
        item.setDurationInMinutes(FacadeCommon.getDateFromMillis(item.getDurationInSeconds() * 1000));
        viewHolder.mDuration.setText(item.getDurationInMinutes());
        viewHolder.mDescription.setText(item.getDescription());
        viewHolder.mName.setText(item.getTitle());

        if (item.isPlaying()) { //to preserve configuration change
            playingPosition = position;
            viewHolder.mPlayPause.setVisibility(View.VISIBLE);
            viewHolder.mDarkLayer.setVisibility(View.VISIBLE);
        }else{
            viewHolder.mPlayPause.setVisibility(View.GONE);
            viewHolder.mDarkLayer.setVisibility(View.GONE);
        }

        String category = mContext.getString(R.string.no_category);
        if (item.getTags().size() > 0) {
            category = item.getTags().get(0).get(KEY_TITLE);
        }
        viewHolder.mCategory.setText(category);

        if (item.getImageFileUrl() != null && item.getImageBitmap()==null) {
            Glide.with(mContext)
                    .load(item.getImageFileUrl())
                    .into(viewHolder.mCoverImage);
            item.setImageBitmap(((BitmapDrawable)viewHolder.mCoverImage.getDrawable()).getBitmap());
        }else {
            if(item.getImageBitmap()!=null){
                viewHolder.mCoverImage.setImageBitmap(item.getImageBitmap());
            }
        }
        viewHolder.mCoverImage.setOnClickListener(getOnClickListener(position, viewHolder, item));
    }

    public void pausePlayer(){
        AudioFile item = mItems.get(playingPosition);
        item.setPlaying(false);
        AudioViewHolder currentlyPlaying = (AudioViewHolder) mFragment.getRecyclerView().findViewHolderForAdapterPosition(playingPosition);
        currentlyPlaying.mPlayPause.setVisibility(View.GONE);
        mFragment.pausePlayer(R.drawable.ic_play_white);
        playingPosition = -1;
    }

//    public void setActiveUserPosition(int position) {
//        if (playingPosition != position) {
//            int oldPosition = playingPosition;
//            playingPosition = position;
//            notifyItemChanged(oldPosition);
//            notifyItemChanged(position);
//        }
//    }

    @NonNull
    private View.OnClickListener getOnClickListener(int position,
                                                    AudioViewHolder viewHolder,
                                                    AudioFile item) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playingPosition != -1 && playingPosition != position) {
                    AudioViewHolder currentlyPlaying = (AudioViewHolder) mFragment.getRecyclerView().findViewHolderForAdapterPosition(playingPosition);
                    if(currentlyPlaying!=null){
                        currentlyPlaying.mDarkLayer.setVisibility(View.GONE);
                        currentlyPlaying.mPlayPause.setVisibility(View.GONE);
                    }
                    mItems.get(playingPosition).setPlaying(false);

                    playingPosition = position;
//                    setActiveUserPosition(position);
                    item.setPlaying(true);
                    mFragment.startPlaying(item);
                    setPlayPauseIcon(viewHolder, item);
                    viewHolder.mPlayPause.setVisibility(View.VISIBLE);
                    viewHolder.mDarkLayer.setVisibility(View.VISIBLE);
                } else if (playingPosition == position) { //playing audio clicked
                    item.setPlaying(!item.isPlaying());
                    int actionType = setPlayPauseIcon(viewHolder, item);
                    if(actionType == 1) {
                        mFragment.pausePlayer(R.drawable.ic_play_white);
                    } else if(actionType==0) {
                        mFragment.pausePlayer(R.drawable.ic_pause_white);
                        mFragment.showPlayer();
                    }
                } else {
                    if(viewHolder.mPlayPause.getVisibility()==View.VISIBLE) {
                        viewHolder.mPlayPause.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_pause_blue));
                    }
                    playingPosition = position;
//                    setActiveUserPosition(position);
                    item.setPlaying(true);
                    mFragment.startPlaying(item);
                    viewHolder.mDarkLayer.setVisibility(View.VISIBLE);
                    viewHolder.mPlayPause.setVisibility(View.VISIBLE);
                }
            }
        };

    }

    private int setPlayPauseIcon(AudioViewHolder viewHolder, AudioFile item) {
        int actionType = 1;
        int icon = R.drawable.ic_play_blue;
        if(item.isPlaying()){
            icon = R.drawable.ic_pause_blue;
            actionType = 0;
        }
        viewHolder.mPlayPause.setImageDrawable(mContext.getResources().getDrawable(icon));
        return actionType;
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private class AudioViewHolder extends RecyclerView.ViewHolder {

        ImageView mCoverImage;
        TextView mCategory;
        TextView mName;
        TextView mDescription;
        TextView mDuration;
        ImageView mPlayPause;
        View      mDarkLayer;


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
