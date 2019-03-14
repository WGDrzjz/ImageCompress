package com.wgd.gdcp.gdcplibrary;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.wgd.gdcp.gdcplibrary.thread.ThreadManager;

import java.util.ArrayList;
import java.util.List;

/*
* change the picture width and height
* Single picture compression
*
* Please apply for permission dynamically
 * android.permission.WRITE_EXTERNAL_STORAGE
 * android.permission.READ_EXTERNAL_STORAGE
*
* 这个处理方式中的优点是：
* 使用单线程线程池，并将Bitmap设为全局变量
* 只要只有一个GDCompressA实例
* 那么循环调用压缩很多图片时大大降低了OOM的几率
* 缺点是：
* 这种方式导致批量压缩时线性的，处理时间将被拉长
*
* */
public class GDCompressA {

    private Context mContext;
    private GDCompressImageListenerA mGDCompressImageListener;

    Bitmap bitmapMin = null;

    public GDCompressA(Context context, GDCompressImageListenerA mGDCompressImageListener){
        this.mContext = context ;
        this.mGDCompressImageListener = mGDCompressImageListener ;
    }

    public GDCompressA(Context context, GDImageBean  mGDImageBean, GDCompressImageListenerA mGDCompressImageListener){
        this.mContext = context ;
        this.mGDCompressImageListener = mGDCompressImageListener ;
        start(mGDImageBean);
    }

    public void start(GDImageBean  mGDImageBean){
        GDConfig gdConfig = null;
        if (null!=mGDImageBean) gdConfig = mGDImageBean.getmGDConfig();
        if (null==gdConfig)gdConfig = new GDConfig();
        if (!GDTools.ImageTesting(gdConfig.getmPath())){
            InformCallError(1, "Incorrect picture format!", mGDImageBean);
            return;
        }
        final GDConfig mGDConfig = gdConfig ;
        if (null==mGDConfig.getSavePath() || TextUtils.equals("", mGDConfig.getSavePath())){
            mGDConfig.setSavePath(mGDConfig.getmPath());
        }
        mGDImageBean.setmGDConfig(mGDConfig);
        final GDImageBean  gdImageBean = mGDImageBean;
        ThreadManager.getIO1().execute(new Runnable() {
            @Override
            public void run() {
                if (mGDConfig.isChangeWH()) {
                    if (mGDConfig.getWidth() <= 0 || mGDConfig.getHeight() <= 0) {
                        try {
                            bitmapMin = new GDCompressUtil().SysCompressMin(mGDConfig.getmPath());
//                            bitmapMin= new GDCompressUtil().SysCompressMin(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (null== bitmapMin){
                            InformCallError(0, "Image compression failure!", gdImageBean);
                        }else {
                            if (new GDCompressUtil().compressLibJpeg(bitmapMin, mGDConfig.getSavePath())) {
                                InformCallSuccess(mGDConfig.getSavePath(), gdImageBean);
                            } else {
                                InformCallError(0, "Image compression failure!", gdImageBean);
                            }
                        }

                    } else {

                        try {
                            bitmapMin = new GDCompressUtil().SysCompressMySamp(mGDConfig.getmPath(), mGDConfig.getWidth(), mGDConfig.getHeight());
//                            bitmapMin= new GDCompressUtil().SysCompressMin(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (null== bitmapMin){
                            InformCallError(0, "Image compression failure!", gdImageBean);
                        }else {
                            if (new GDCompressUtil().compressLibJpeg(bitmapMin, mGDConfig.getSavePath())) {
                                InformCallSuccess(mGDConfig.getSavePath(), gdImageBean);
                            } else {
                                InformCallError(0, "Image compression failure!", gdImageBean);
                            }
                        }

                    }
                } else {
//                    Bitmap bitmap = null;
                    try {
                        bitmapMin = GDBitmapUtil.getBitmap(mGDConfig.getmPath());
                    } catch (Exception e) {
                        e.printStackTrace();
                        bitmapMin = BitmapFactory.decodeFile(mGDConfig.getmPath());
                    }

                    if (null== bitmapMin){
                        InformCallError(0, "Image compression failure!", gdImageBean);
                    }else
                    if (new GDCompressUtil().compressLibJpeg(bitmapMin, mGDConfig.getSavePath())) {
                        InformCallSuccess(mGDConfig.getSavePath(), gdImageBean);
                    } else {
                        InformCallError(0, "Image compression failure!", gdImageBean);
                    }
                }
            }
        });
    }

    private void InformCallSuccess(final String path, GDImageBean  mGDImageBean){
        try {
            GDBitmapUtil.saveBitmapDegree(path);
        }catch (Exception e){e.printStackTrace();}
        try {
            if (null!=bitmapMin ){
                bitmapMin.recycle();
                bitmapMin = null;
                System.gc();
            }
        }catch (Exception e){e.printStackTrace();}
        mGDImageBean.setCode(0);
        try {
            final GDImageBean  gDImageBean =  mGDImageBean;
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (null!= mGDCompressImageListener) mGDCompressImageListener.OnSuccess(gDImageBean);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            if (null!= mGDCompressImageListener) mGDCompressImageListener.OnSuccess(mGDImageBean);
        }
    }
    private void InformCallError(final int code, final String errorMsg, final GDImageBean  mGDImageBean){
        try {
            if (null!=bitmapMin ){
                bitmapMin.recycle();
                bitmapMin = null;
                System.gc();
            }
        }catch (Exception e){e.printStackTrace();}
        mGDImageBean.setCode(code);
        mGDImageBean.setErrorMsg(errorMsg);
        try {
            final GDImageBean  gDImageBean =  mGDImageBean;
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (null!= mGDCompressImageListener) mGDCompressImageListener.OnError(gDImageBean);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            if (null!= mGDCompressImageListener) mGDCompressImageListener.OnError(mGDImageBean);
        }
    }


}
