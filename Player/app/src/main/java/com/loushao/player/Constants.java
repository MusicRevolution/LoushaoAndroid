package com.loushao.player;

public class Constants {
    static {
        System.loadLibrary("native-lib");
    }
    public static final native String baseUrl();
}
