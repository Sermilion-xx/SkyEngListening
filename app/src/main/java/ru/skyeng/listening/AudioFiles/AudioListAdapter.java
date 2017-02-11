package ru.skyeng.listening.AudioFiles;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.skyeng.listening.AudioFiles.domain.AudioFile;
import ru.skyeng.listening.R;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 10/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class AudioListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<AudioFile> mItems;
    private Context mContext;

    public AudioListAdapter(Context context){
        this.mContext = context;
        mItems = new ArrayList<>();
    }

    private AudioListAdapter(){

    }

    public void setAudioFiles(List<AudioFile> data){
        mItems = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewRow =  inflater.inflate(R.layout.list_item_audio_file, parent, false);
        return new AudioViewHolder(viewRow);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AudioViewHolder viewHolder = (AudioViewHolder) holder;
        AudioFile item = mItems.get(position);

        viewHolder.mDuration.setText(getDateFromMillis(item.getDurationInSeconds()*1000));
        viewHolder.mDescription.setText(item.getDescription());
        viewHolder.mName.setText(item.getTitle());
        String category = "Нет категории";
        if(item.getTags().size()>0){
            category = item.getTags().get(0).get("title");
        }
        viewHolder.mCategory.setText(category);
        if(item.getImageFileUrl()!=null) {
            Glide.with(mContext)
                    .load(item.getImageFileUrl())
                    .into(viewHolder.mCoverImage);
        }
    }

    public static String getDateFromMillis(long millis) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss", Locale.getDefault());
        return formatter.format(new Date(millis));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private class AudioViewHolder extends RecyclerView.ViewHolder{

        ImageView mCoverImage;
        TextView  mCategory;
        TextView  mName;
        TextView  mDescription;
        TextView  mDuration;


        AudioViewHolder(View itemView) {
            super(itemView);
            mCoverImage = (ImageView) itemView.findViewById(R.id.image_cover);
            mCategory   = (TextView) itemView.findViewById(R.id.text_category);
            mName       = (TextView) itemView.findViewById(R.id.text_name);
            mDescription= (TextView) itemView.findViewById(R.id.text_description);
            mDuration   = (TextView) itemView.findViewById(R.id.text_length);
        }
    }
}
