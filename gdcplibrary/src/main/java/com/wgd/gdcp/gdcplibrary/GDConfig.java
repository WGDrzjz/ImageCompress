package com.wgd.gdcp.gdcplibrary;

public class GDConfig {

    /*
    * Whether you want to change the picture width and height, so that the image compression is smaller.
    * */
    private boolean isChangeWH = false ;//
    private int width = 0 ;
    private int height = 0 ;

    private String mPath ;
    private String savePath ;
//    private GDCompressImageListener mGDCompressImageListener;

    public boolean isChangeWH() {
        return isChangeWH;
    }

    public GDConfig setChangeWH(boolean changeWH) {
        isChangeWH = changeWH;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public GDConfig setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public GDConfig setHeight(int height) {
        this.height = height;
        return this;
    }

    public String getmPath() {
        return mPath;
    }

    public GDConfig setmPath(String mPath) {
        this.mPath = mPath;
        return this;
    }

    public String getSavePath() {
        return savePath;
    }

    public GDConfig setSavePath(String savePath) {
        this.savePath = savePath;
        return this;
    }

//    public GDCompressImageListener getmGDCompressImageListener() {
//        return mGDCompressImageListener;
//    }
//
//    public GDConfig setmGDCompressImageListener(GDCompressImageListener mGDCompressImageListener) {
//        this.mGDCompressImageListener = mGDCompressImageListener;
//        return this;
//    }
}
