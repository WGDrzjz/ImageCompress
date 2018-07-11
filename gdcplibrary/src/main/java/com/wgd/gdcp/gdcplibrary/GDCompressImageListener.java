package com.wgd.gdcp.gdcplibrary;

/*
* 压缩回调接口
* */
public interface GDCompressImageListener {

    void OnSuccess(String path);

    void OnError(int code, String errorMsg);

}
