package com.example.mediatest.bean;

public class Metadata {

    /**
     * hash : E3CC45C5F97526DF9955A016607552A8D8FA6DD8
     * exist : true
     * name : [Amon_Saga][R1DVDRip][OVA][X264_AAC].mkv
     * size : 405665581
     */

    private String hash;
    private boolean exist;
    private String name;
    private int size;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
