package com.loushao.player.player;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.loushao.player.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jzvd.JZVideoPlayer;
import master.flame.danmaku.ui.widget.DanmakuView;

public class MyVideoPlayer extends JZVideoPlayer {
    @BindView(R.id.thumb)
    ImageView thumb;
    @BindView(R.id.current)
    TextView current;
    @BindView(R.id.bottom_seek_progress)
    SeekBar bottomSeekProgress;
    @BindView(R.id.total)
    TextView total;
    @BindView(R.id.danmu)
    ImageView danmu;
    @BindView(R.id.fullscreen)
    ImageView fullscreen;
    @BindView(R.id.layout_bottom)
    LinearLayout layoutBottom;
    @BindView(R.id.bottom_progress)
    ProgressBar bottomProgress;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.battery_level)
    ImageView batteryLevel;
    @BindView(R.id.video_current_time)
    TextView videoCurrentTime;
    @BindView(R.id.battery_time_layout)
    LinearLayout batteryTimeLayout;
    @BindView(R.id.layout_top)
    RelativeLayout layoutTop;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.start)
    ImageView start;
    @BindView(R.id.start_layout)
    LinearLayout startLayout;
    @BindView(R.id.replay_text)
    TextView replayText;
    @BindView(R.id.retry_btn)
    TextView retryBtn;
    @BindView(R.id.retry_layout)
    LinearLayout retryLayout;
    @BindView(R.id.danmaku_view)
    DanmakuView danmakuView;
    Unbinder unbinder;

    public MyVideoPlayer(Context context) {
        super(context);
    }

    public MyVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        View view = View.inflate(context, getLayoutId(), this);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_player;
    }

    @OnClick({R.id.thumb, R.id.danmu, R.id.fullscreen, R.id.back, R.id.retry_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.thumb:
                break;
            case R.id.danmu:
                break;
            case R.id.fullscreen:
                break;
            case R.id.back:
                break;
            case R.id.retry_btn:
                break;
        }
    }
}
