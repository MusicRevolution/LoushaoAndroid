package com.example.mediatest;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.WindowManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.JZMediaManager;

public class PlayerActivity extends AppCompatActivity {
    String path;
    String danmuPath = null;
    @BindView(R.id.player)
    MyVideoPlayer player;
    private long exitTime=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);
        path = getIntent().getStringExtra("path");
        danmuPath = getIntent().getStringExtra("danmupath");
        //Log.w("onCreate: ", danmuPath+"");

        player.setUp(path, MyVideoPlayer.SCREEN_WINDOW_FULLSCREEN, "");
        player.startVideo();
        if (danmuPath != null) {
            player.setDanmuPath(danmuPath);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            JZMediaManager.pause();
            player.onStatePause();
          if ((System.currentTimeMillis() - exitTime) > 2000) {//
             // 如果两次按键时间间隔大于2000毫秒，则不退出
              exitTime = System.currentTimeMillis();// 更新mExitTime
          } else {
              finish();
          }
          return true;
      }
      return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onResume() {
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }
}
