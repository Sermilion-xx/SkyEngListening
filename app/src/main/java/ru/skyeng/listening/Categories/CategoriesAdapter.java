package ru.skyeng.listening.Categories;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.skyeng.listening.Categories.model.AudioTag;
import ru.skyeng.listening.Categories.model.TagsData;
import ru.skyeng.listening.Categories.model.TagsRequestParams;
import ru.skyeng.listening.MVPBase.MVPPresenter;
import ru.skyeng.listening.R;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 15/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class CategoriesAdapter extends BaseAdapter{

    private MVPPresenter<TagsData, List<AudioTag>, TagsRequestParams> mPresenter;
    private CategoriesFragment mFragment;
    private LayoutInflater inflater;


    void setContext(Context context){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private List<AudioTag> getItems() {
        return mPresenter.getModel().getItems();
    }

    void setPresenter(MVPPresenter<TagsData, List<AudioTag>, TagsRequestParams> mPresenter) {
        this.mPresenter = mPresenter;
    }

    void setFragment(CategoriesFragment mFragment) {
        this.mFragment = mFragment;
    }

    private Context getContext() {
        return mPresenter.getActivityContext();
    }


//    @Override
//    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        View viewRow = inflater.inflate(R.layout.list_item_tag, parent, false);
//        return new TagViewHolder(viewRow);
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
//        TagViewHolder holder = (TagViewHolder) viewHolder;
//        AudioTag item = getItems().get(position);
//        holder.title.setText(item.getTitle());
//        holder.title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!item.isSelected()){
//                    holder.title.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBlue2));
//                    mFragment.addSelectedId(item.getId());
//                }else {
//                    holder.title.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBlue3));
//                    mFragment.removeSelectedId(item.getId());
//                }
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return mPresenter.getData()==null?0:mPresenter.getData().size();
//    }

    @Override
    public int getCount() {
        return mPresenter.getData()==null?0:mPresenter.getData().size();
    }

    @Override
    public Object getItem(int position) {
        return mPresenter.getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TagViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_tag, null);
            holder = new TagViewHolder(convertView);
        }else {
            holder = (TagViewHolder) convertView.getTag();
        }
        AudioTag item = getItems().get(position);
        holder.title.setText(item.getTitle());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!item.isSelected()){
                    holder.title.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBlue2));
                    mFragment.addSelectedId(item.getId());
                }else {
                    holder.title.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBlue3));
                    mFragment.removeSelectedId(item.getId());
                }
            }
        });
        convertView.setTag(holder);
        return convertView;
    }

    public class TagViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public TagViewHolder(View viewRow) {
            super(viewRow);
            title = (TextView) viewRow.findViewById(R.id.title);
        }
    }
}
