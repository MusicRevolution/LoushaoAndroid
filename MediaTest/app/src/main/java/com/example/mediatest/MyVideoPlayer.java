package com.example.mediatest;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.JZUtils;
import cn.jzvd.JZVideoPlayerStandard;
import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.ui.widget.DanmakuView;

public class MyVideoPlayer extends JZVideoPlayerStandard {
    @BindView(R.id.danmaku_view)
    DanmakuView danmakuView;
    @BindView(R.id.danmu)
    ImageView danmu;
    @BindView(R.id.back1)
    ImageView back1;
    private boolean showDanmu;
    private DanmakuContext danmakuContext;
    private BaseDanmakuParser mParser;
    private InputStream in;
    private String danmuPath;

    @Override
    public int getLayoutId() {
        return R.layout.player_layout;
    }

    public void setDanmuPath(String path) {
        danmuPath = path;
        loadDanmuFile();
        setDanmakuView();
    }

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
        backButton.setClickable(false);
        fullscreenButton.setClickable(false);

    }

    private void loadDanmuFile() {
        try {
            in = new FileInputStream(new File(danmuPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setDanmakuView() {
        danmakuContext = DanmakuContext.create();
        if (danmakuView != null) {
            mParser = createParser(in);
            danmakuView.setCallback(new DrawHandler.Callback() {
                @Override
                public void prepared() {
                    danmakuView.start();
                }

                @Override
                public void updateTimer(DanmakuTimer timer) {

                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {

                }

                @Override
                public void drawingFinished() {

                }
            });
        }
        danmakuView.prepare(mParser, danmakuContext);
        danmakuView.showFPS(false);
    }


    private BaseDanmakuParser createParser(InputStream stream) {
        if (stream == null) {
            return new BaseDanmakuParser() {
                @Override
                protected IDanmakus parse() {
                    return new Danmakus();
                }
            };
        }
        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);
        try {
            loader.load(stream);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        BaseDanmakuParser parser = new BiliDanmukuParser();
        IDataSource<?> dataSource = loader.getDataSource();
        parser.load(dataSource);
        return parser;
    }



    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        if (danmakuView != null) {
            danmakuView.resume();
        }

    }

    @Override
    public void onStatePause() {
        super.onStatePause();
        if (danmakuView != null) {
            danmakuView.pause();
        }

    }

    @Override
    public void startVideo() {
        super.startVideo();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (danmakuView != null && danmakuView.getConfig() != null) {
                    long time = JZUtils.getSavedProgress(getContext(), JZUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex));
                    Log.w(TAG, "startVideo: " + time);
                    danmakuView.seekTo(time);
                }
            }
        }).start();


    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        super.onProgressChanged(seekBar, progress, fromUser);
        if (fromUser) {
            long time = seekBar.getProgress() * getDuration() / 100;
            if (danmakuView != null && danmakuView.getConfig() != null) {
                danmakuView.seekTo(time);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        if (id == cn.jzvd.R.id.surface_container) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    startDismissControlViewTimer();
                    if (mChangePosition) {
                        long duration = getDuration();
                        int progress = (int) (mSeekTimePosition * 100 / (duration == 0 ? 1 : duration));
                        bottomProgressBar.setProgress(progress);
                        long time = progress * duration / 100;

                        if (danmakuView != null && danmakuView.getConfig() != null) {
                            danmakuView.seekTo(time);
                        }

                    }
                    break;
            }
        }
        return super.onTouch(v, event);
    }

    private void hideDanmu() {
        showDanmu = true;
        danmu.setImageResource(R.drawable.ic_danmaku_closed);
        if (danmakuView != null) {
            danmakuView.hide();
        }

    }

    private void showDanmu() {
        danmu.setImageResource(R.drawable.ic_danmaku_open);
        showDanmu = false;
        if (danmakuView != null) {
            danmakuView.show();
        }

    }

    @OnClick({R.id.danmu, R.id.back1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.danmu:
                if (danmakuView != null) {
                    if (showDanmu) {
                        showDanmu();
                    } else {
                        hideDanmu();
                    }
                }
                break;
            case R.id.back1:
                //Log.w(TAG, "onViewClicked: " );
                break;
        }
    }
}
