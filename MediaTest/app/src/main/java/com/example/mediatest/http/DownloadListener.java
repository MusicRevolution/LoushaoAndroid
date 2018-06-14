package com.example.mediatest.http;

public interface DownloadListener {
    void download(int i,DownloadListener listener);
    void updata(int progress);
}
