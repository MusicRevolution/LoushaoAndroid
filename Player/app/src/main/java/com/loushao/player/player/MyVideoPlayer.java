package com.loushao.player.player;

import android.content.Context;
import android.util.AttributeSet;

import com.loushao.player.R;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import butterknife.ButterKnife;
public class MyVideoPlayer extends StandardGSYVideoPlayer {


    public MyVideoPlayer(Context context) {
        super(context);
    }

    public MyVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        ButterKnife.bind(this);
    }


    @Override
    public int getLayoutId() {
        return R.layout.layout_player;
    }

}
