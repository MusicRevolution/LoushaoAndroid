package com.example.mediatest.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.mediatest.DownloadActivity;
import com.example.mediatest.model.GetUrlListener;
import com.example.mediatest.model.GetUrlModel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadTask {
    private String Url;
    private int number;
    private File file;
    private int blocksize;
    private int downLength = 0;
    private int endSize;
    private Context context;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    GetUrlListener listener;
    Timer timer;
    public DownloadTask(String url, int blocksize, int totalsize, File file, Context context) {
        this.Url = url;
        this.blocksize = blocksize;
        this.number = totalsize / blocksize;
        this.file = file;
        this.endSize = totalsize;
        this.context=context;

    }

    public void doDownload() {
        download(0);
        listener=new GetUrlListener() {
            @Override
            public void onSuccess(String url) {
                Url=url;
                //Log.e("onSuccess", " " + Url);
            }

            @Override
            public void onFailure(String msg) {

            }
        };

       // pref=context.getSharedPreferences("downloadLength",Context.MODE_PRIVATE);
       // downLength=pref.getInt("downloadLength",0);
        timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                getDownUrl();
                //editor=context.getSharedPreferences("downloadLength",Context.MODE_PRIVATE).edit();
                //editor.putInt("downLength",downLength);
                //editor.apply();
            }
        };
        timer.schedule(timerTask,300000,300000);

    }
//TODO 防止手机锁屏回收 放在service里
    public void download(final int i) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient.Builder()
                        .readTimeout(20, TimeUnit.SECONDS)
                        .build();

                //int startPos = blocksize * i;
                final int endPos;
                //Log.e("run: ", i+"   "+number);
                if (number == i) {
                    timer.cancel();
                    endPos = endSize;
                } else {
                    endPos = blocksize * (i+1) - 1;
                }
                //Log.e("onSuccessrun: ", Url +"");
                Request request = new Request.Builder()
                        .url(Url)
                        .header("Range", "bytes=" + downLength + "-" + endPos)
                        .build();
                Log.e("run: ", downLength+"   "+endPos);
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        BufferedInputStream bis = null;
                        RandomAccessFile raf = null;
                        byte[] buffer = new byte[1024];
                        InputStream in = response.body().byteStream();
                        bis = new BufferedInputStream(in);
                        //能对file进行随意读写
                        raf = new RandomAccessFile(file, "rwd");
                        //移到所处该线程所应使用的位置
                        raf.seek(downLength);
                        int len;
                        //每次最多读1024个byte
                        while ((len = bis.read(buffer, 0, 1024)) != -1) {
                            //在buffer中写len个字节到文件file中
                            raf.write(buffer, 0, len);
                            downLength += len;
                            //Log.e("run: ", downLength + "");
                            DownloadActivity.updataProgress(downLength);
                        }
                        //Log.e("run:111 ", endPos + "");
                        if (downLength==endPos+1){
                            if (i<number)
                            download(i+1);
                        }
                    }

                });

            }
        }).start();

    }

    private void getDownUrl() {
        GetUrlModel model = new GetUrlModel();
        model.setListener(listener);
        model.getUrl();
    }
}
