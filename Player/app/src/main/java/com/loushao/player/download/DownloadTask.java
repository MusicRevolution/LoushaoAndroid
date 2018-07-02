package com.loushao.player.download;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadTask {
    private static final String TAG = "DownloadTask";
    private String url;
    private File file;
    private long totalsize;
    private Context context;
    private int blocksize;
    private int currentSize=0;
    private int[] total;
    private int num;
    public DownloadTask(Context context, File file, String url) {
        this.context = context;
        this.file = file;
        this.url = url;
        //this.totalsize = totalsize;
    }
    public void setNumber(int num){
        total = new int[num];
    };
    public void doDownload(int a) {
        totalsize= getSize();
        blocksize = (int)totalsize / a;
        for (int i = 0; i < a; i++) {
            int startPos = blocksize * i;
            int endPos = blocksize * (i + 1) - 1;
            if (i==a-1){
                endPos=(int)totalsize-1;
            }
            download(i,startPos, endPos);
        }
    }
    public int getCurrentSize(){
        int a = 0;
        for (int i=0;i<total.length;i++){
            a+=total[i];
        }
        currentSize=a;
        return currentSize;
    }
    private void download(final int i, final long startPos, final long endPos) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient.Builder()
                        .readTimeout(10, TimeUnit.SECONDS)
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .header("Range", "bytes=" + startPos + "-" + endPos)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        RandomAccessFile raf = new RandomAccessFile(file, "rwd");
                        InputStream is = response.body().byteStream();
                        raf.setLength(totalsize);
                        raf.seek(startPos);
                        byte[] b = new byte[1024*4];
                        int len;

                        while ((len = is.read(b)) != -1) {
                            raf.write(b, 0, len);
                            //currentSize+=len;
                            total[i]+=len;
                            //Log.e(TAG, "onResponse: "+i+"    "+total[i] );
                        }
                        raf.close();
                        is.close();
                    }
                });
            }
        }).start();
    }
    public long getSize(){
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        try {
            totalsize=call.execute().body().contentLength();
            Log.e("000: ", totalsize+" ");
            return  totalsize;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
