package com.loushao.player.download;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.loushao.player.Constants;
import com.loushao.player.R;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class DownloadService extends Service {
    NotificationManager manager;
    NotificationCompat.Builder notificationBuilder;
    Notification notification;
    private int id;
    private long size;
    private String url;
    private String title;
    private File file;
    private TimerTask timerTask;
    private Timer timer;
    private DownloadTask downloadTask;
    private final int updata = 1;
    private final int success = 0;
    private int currentSize;
    private int threadcount=4;//在这修改下载的线程数
    public DownloadService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        title = intent.getStringExtra("title");
        url = intent.getStringExtra("url");
        //size = intent.getIntExtra("size", 0);
        id = intent.getIntExtra("id", 0);
        init();
        return super.onStartCommand(intent, flags, startId);
    }

    private void init() {
        file = new File(Constants.basePath + title);
        downloadTask = new DownloadTask(this, file, url);
        downloadTask.setNumber(threadcount);
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationBuilder = new NotificationCompat.Builder(this);
        notification = notificationBuilder.setContentTitle("开始下载")
                .setContentText(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setProgress(100, 0, false)
                .build();
        manager.notify(id, notification);
        new Thread(new Runnable() {
            @Override
            public void run() {
                size = downloadTask.getSize();
                downloadTask.doDownload(threadcount);
            }
        }).start();

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (downloadTask.getCurrentSize() > 0) {
                    Log.e("222333: ", downloadTask.getCurrentSize() + "   " + size);
                    if (downloadTask.getCurrentSize() >= size) {
                        handler.sendEmptyMessage(success);
                    } else {
                        handler.sendEmptyMessage(updata);
                    }
                }

            }
        };
        timer.schedule(timerTask, 500, 500);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case updata:
                    currentSize = downloadTask.getCurrentSize();
                    float percent = (float) currentSize / size * 100;
                    notification = notificationBuilder
                            .setProgress(100, (int) percent, false)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("正在下载..." + percent + "%")
                            .build();
                    manager.notify(id, notification);
                    break;
                case success:
                    notification = notificationBuilder
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("下载完成")
                            .setProgress(100, 100, false)
                            .build();
                    manager.notify(id, notification);
                    if (timer != null && timerTask != null) {
                        timer.cancel();
                        timerTask.cancel();
                    }
                    break;
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
