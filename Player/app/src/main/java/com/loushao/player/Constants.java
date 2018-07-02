package com.loushao.player;

import android.os.Environment;

public class Constants {
    static {
        System.loadLibrary("native-lib");
    }
    public static final native String baseUrl();
    public static String basePath= Environment.getExternalStorageDirectory().getAbsolutePath() + "/LouShao/";
}
