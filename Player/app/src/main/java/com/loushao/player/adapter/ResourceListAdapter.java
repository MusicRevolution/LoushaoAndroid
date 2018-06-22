package com.loushao.player.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loushao.player.R;
import com.loushao.player.bean.AnimeDetail;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResourceListAdapter extends BaseAdapter {
    private Activity activity;
    private List<AnimeDetail.ResourcesBean> list;

    public ResourceListAdapter(Activity activity, List<AnimeDetail.ResourcesBean> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(activity).inflate(R.layout.item_resourcelist, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.animeName.setText(list.get(i).getTitle());
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.anime_name)
        TextView animeName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
