package com.wgd.myimagelibrary;

public class ImageInfoBean {

    private int[] wightHeightSize;
    private String filepath ;
    private long fileSize ;

    public ImageInfoBean(){}
    public ImageInfoBean(int[] wightHeightSize, String filepath, long fileSize){
        this.wightHeightSize = wightHeightSize;
        this.filepath = filepath;
        this.fileSize = fileSize;
    }

    public int[] getWightHeightSize() {
        return wightHeightSize;
    }

    public void setWightHeightSize(int[] wightHeightSize) {
        this.wightHeightSize = wightHeightSize;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
