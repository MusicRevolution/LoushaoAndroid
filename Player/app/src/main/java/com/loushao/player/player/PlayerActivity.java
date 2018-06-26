package com.loushao.player.player;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.loushao.player.R;
import com.loushao.player.bean.Url;
import com.loushao.player.network.RetrofitFactory;
import com.shuyu.gsyvideoplayer.GSYBaseActivityDetail;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.model.VideoOptionModel;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class PlayerActivity extends GSYBaseActivityDetail<MyVideoPlayer> {
    private static final String TAG = "PlayerActivity";
    @BindView(R.id.player)
    MyVideoPlayer player;
    boolean star = false;
    @BindView(R.id.img_Star)
    ImageView imgStar;
    private String hash;
    private int size;
    private String title;
    private String downurl;
    public static void start(Context context, String title, String hash, int size) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("hash", hash);
        intent.putExtra("size", size);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        hash = intent.getStringExtra("hash");
        size = intent.getIntExtra("size", 0);
        Log.e(TAG, "onCreate: " + title + "\n" + hash + "\n" + size);
        youhua();
        getUrl(hash);
    }

    private void youhua(){
        GSYVideoManager.instance().setVideoType(this, GSYVideoType.IJKPLAYER);
        //优化视频卡顿
        VideoOptionModel videoOptionModel =
                new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 50);
        List<VideoOptionModel> list = new ArrayList<>();
        list.add(videoOptionModel);
        GSYVideoManager.instance().setOptionModelList(list);
    }
    @Override
    public MyVideoPlayer getGSYVideoPlayer() {
        return player;
    }

    @Override
    public GSYVideoOptionBuilder getGSYVideoOptionBuilder() {
        return new GSYVideoOptionBuilder()
                .setUrl(downurl)
                .setVideoTitle(title)
                .setCacheWithPlay(false)//视频卡顿关缓存
                .setSeekRatio(1)
                .setNeedLockFull(true)
                .setIsTouchWiget(true)
                .setShowFullAnimation(false)
                .setRotateViewAuto(false)
                .setLockLand(false);
    }

    @Override
    public void clickForFullScreen() {

    }
   //是否启动旋转横屏，true表示启动
    @Override
    public boolean getDetailOrientationRotateAuto() {
        return true;
    }


    @OnClick({R.id.btn_Down, R.id.img_Star})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_Down:
                Toast.makeText(this,"还没写",Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_Star:
                if (star) {
                    imgStar.setImageResource(android.R.drawable.btn_star_big_off);
                    star = false;
                } else {
                    imgStar.setImageResource(android.R.drawable.btn_star_big_on);
                    star = true;
                }
                break;
        }
    }
    private void getUrl(String hash) {
        Observable<Url> request = RetrofitFactory.getInstance().getUrl(hash);
        request.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Function<Throwable, Url>() {
                    @Override
                    public Url apply(Throwable throwable) throws Exception {
                        Log.e(TAG, "apply: " + throwable);
                        return null;
                    }
                })
                .subscribe(new Consumer<Url>() {
                    @Override
                    public void accept(Url url) throws Exception {
                        if (url.getError() == null) {
                            Log.e(TAG, "accept111: " + url.getRedirectUrl());
                            downurl=url.getRedirectUrl();
                            initVideoBuilderMode();
                        }
                    }
                });
    }
}
