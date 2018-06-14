package com.example.mediatest;

import android.os.Environment;

public class Constant {
    public static String basePath= Environment.getExternalStorageDirectory().getAbsolutePath() + "/LouShao";
    public static String token=BuildConfig.TOKEN;
    public static String hash=BuildConfig.HASH;
    public static String baseDownUrl="http://loushaomaster.chinacloudsites.cn/file/";
}
