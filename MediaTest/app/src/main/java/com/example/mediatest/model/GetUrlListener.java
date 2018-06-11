package com.example.mediatest.model;

public interface GetUrlListener {
    void onSuccess(String url);
    void onFailure(String msg);
}
