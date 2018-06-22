package com.loushao.player.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loushao.player.R;
import com.loushao.player.bean.ResourceList;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListViewAdapter extends BaseAdapter {
    private Activity activity;
    private List<ResourceList.DataBean> list;

    public ListViewAdapter(Activity activity, List<ResourceList.DataBean> list) {
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
            view = LayoutInflater.from(activity).inflate(R.layout.item_list, null);
            viewHolder=new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.txtTitle.setText(list.get(i).getTitle());
        viewHolder.txtIntro.setText(list.get(i).getIntro());
        Glide.with(activity).load(list.get(i).getSmall_img()).into(viewHolder.imgSmall);
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.img_Small)
        ImageView imgSmall;
        @BindView(R.id.txt_Title)
        TextView txtTitle;
        @BindView(R.id.txt_Intro)
        TextView txtIntro;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
