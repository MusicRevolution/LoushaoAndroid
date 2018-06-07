package com.example.mediatest;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.provider.MediaStore.Video.Thumbnails.MINI_KIND;

public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private List<Movie> list;

    public ListViewAdapter(Context context, List<Movie> list) {
        this.context = context;
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
            view = LayoutInflater.from(context).inflate(R.layout.list_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(list.get(i).getTitle());
        String path = list.get(i).getPath();
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MINI_KIND);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, 300, 180,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        viewHolder.imageView.setImageBitmap(bitmap);

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.textView)
        TextView textView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
