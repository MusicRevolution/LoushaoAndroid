package com.loushao.player.login;

public interface LoginListener {
    void onSuccess(String token);
    void onFailure(String msg);
}
