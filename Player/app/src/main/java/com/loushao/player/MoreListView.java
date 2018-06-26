package com.loushao.player;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

public class MoreListView extends ListView implements AbsListView.OnScrollListener {
    private Context context;
    private View footView;
    private int totalCount;
    private boolean isLoading=false;
    private OnLoadListener loadListener;
    public MoreListView(Context context) {
        super(context);
        init(context);
    }

    public MoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.context=context;
        footView= LayoutInflater.from(context).inflate(R.layout.item_footview,null);
        setOnScrollListener(this);
    }


    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        // 滑到底部后自动加载，判断listview已经停止滚动并且最后可视的条目等于adapter的条目
        int lastVisibleIndex=absListView.getLastVisiblePosition();
        if (!isLoading&&i==OnScrollListener.SCROLL_STATE_IDLE&&lastVisibleIndex==totalCount-1){
            isLoading=true;
            addFooterView(footView);
            if (loadListener!=null){
                loadListener.onLoadMore();
            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        totalCount=i2;
    }
    public interface OnLoadListener{
        void onLoadMore();
    }
    public void setOnLoadListener(OnLoadListener listener){
        loadListener=listener;
    }
    public void setLoadCompleted(){
        isLoading=false;
        removeFooterView(footView);
    }
}
