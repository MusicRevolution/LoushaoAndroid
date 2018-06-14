package com.example.mediatest.http;

import android.content.Context;

import com.example.mediatest.Constant;

import java.io.File;

public class DownloadManager {
    private int size;
    private String url;
    private String name="test2.mkv";//metadata里有名字
    private String filePath= Constant.basePath+name;
    private int totalsize=94290242;
    public void setBytes(int size) {
        this.size=size;
    }
    public void setUrl(String url){
        this.url=url;
    }
    public void download(final Context context){
        final File file=new File(filePath);

        new Thread(new Runnable() {
            @Override
            public void run() {
                DownloadTask task=new DownloadTask(url,size,totalsize,file,context);
                task.doDownload();
            }
        }).start();

    }
}
