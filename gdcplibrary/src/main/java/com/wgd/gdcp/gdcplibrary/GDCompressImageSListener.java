package com.wgd.gdcp.gdcplibrary;

import java.util.List;

/*
* 压缩回调接口
* */
public interface GDCompressImageSListener {

    void OnSuccess(List<GDImageBean> imageBeanList);

    void OnError(List<GDImageBean> imageBeanList);

}
