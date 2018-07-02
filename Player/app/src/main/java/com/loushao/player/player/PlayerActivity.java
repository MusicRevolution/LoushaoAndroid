package com.loushao.player.player;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.loushao.player.Constants;
import com.loushao.player.FileUtil;
import com.loushao.player.R;
import com.loushao.player.bean.Url;
import com.loushao.player.download.DownloadService;
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
    @BindView(R.id.btn_Down)
    Button btnDown;
    private String hash;
    private int size;
    private String title;
    private String downurl;
    private int id;
    public final static int STORAGE = 11;

    public static void start(Context context, String title, String hash, int size, int id) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("hash", hash);
        intent.putExtra("size", size);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);
        ButterKnife.bind(this);
        btnDown.setClickable(false);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        hash = intent.getStringExtra("hash");
        size = intent.getIntExtra("size", 0);
        id = intent.getIntExtra("id", 0);
        Log.e(TAG, "onCreate: " + title + "\n" + hash + "\n" + size + "\n" + id);
        youhua();
        getUrl(hash);
    }

    private void youhua() {
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
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    permission();
                }else {
                    doDownLoad();
                }

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
                            Log.e(TAG, "accept: " + url.getRedirectUrl());
                            btnDown.setClickable(true);
                            downurl = url.getRedirectUrl();
                            initVideoBuilderMode();
                        }
                    }
                });
    }

    private void doDownLoad() {
        if (FileUtil.isFileExists(Constants.basePath)){
            Intent intent = new Intent(PlayerActivity.this, DownloadService.class);
            intent.putExtra("title", title);
            intent.putExtra("url", downurl);
            intent.putExtra("size", size);
            intent.putExtra("id", id);
            startService(intent);
        }else {
            Toast.makeText(this, "目录不存在", Toast.LENGTH_SHORT).show();
        }

    }

    private void permission() {
        if (ContextCompat.checkSelfPermission(PlayerActivity.this,
                Manifest.permission_group.STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PlayerActivity.this, new String[]
                    {Manifest.permission_group.STORAGE}, STORAGE);
        } else {
            doDownLoad();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case STORAGE:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    doDownLoad();
                }else {
                    Toast.makeText(this, "权限申请失败", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
