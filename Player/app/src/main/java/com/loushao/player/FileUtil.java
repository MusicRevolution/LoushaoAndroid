package com.loushao.player;

import java.io.File;

public class FileUtil {
    public static boolean isFileExists(String path){
        File file=new File(path);
        if (!file.exists()){
            if (file.mkdir()){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }
}
