package com.loushao.player;

import android.app.Application;

public class MyApplication extends Application {

    public static String token;
    private boolean isAutoLogin;

    public boolean isAutoLogin() {
        return isAutoLogin;
    }

    public void setAutoLogin(boolean autoLogin) {
        isAutoLogin = autoLogin;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    //videocache
    /*
    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        MyApplication app = (MyApplication) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this)
                .maxCacheSize(2047 * 1024 * 1024)//2GB
                .build();
    }*/


}
