package com.loushao.player.player;

import com.shuyu.gsyvideoplayer.GSYBaseActivityDetail;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;

public class StandardVideoPlayer extends GSYBaseActivityDetail<MyVideoPlayer> {
    MyVideoPlayer player;
    @Override
    public MyVideoPlayer getGSYVideoPlayer() {
        return player;
    }

    @Override
    public GSYVideoOptionBuilder getGSYVideoOptionBuilder() {
        return null;
    }

    @Override
    public void clickForFullScreen() {

    }

    @Override
    public boolean getDetailOrientationRotateAuto() {
        return false;
    }
}
