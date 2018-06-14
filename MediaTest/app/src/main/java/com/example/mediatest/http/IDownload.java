package com.example.mediatest.http;

public interface IDownload {
    void onStart();
    void onSuccess();
    void onFinish();
    void onProgress();
}
