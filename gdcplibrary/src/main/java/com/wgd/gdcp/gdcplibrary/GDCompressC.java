package com.wgd.gdcp.gdcplibrary;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/*
* change the picture width and height
* */
public class GDCompressC {

    private Context mContext;
    private GDConfig mGDConfig;
    private GDCompressImageListener mGDCompressImageListener;

    private List<Bitmap> garbage = new ArrayList<>();

    public GDCompressC(Context context, GDConfig mGDConfig, GDCompressImageListener mGDCompressImageListener){
        this.mContext = context ;
        this.mGDConfig = mGDConfig ;
        this.mGDCompressImageListener = mGDCompressImageListener ;
        start();
    }

    public void start(){
        if (null==mGDConfig)mGDConfig = new GDConfig();
        initSavePath();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mGDConfig.isChangeWH()){
                    if (mGDConfig.getWidth() <=0 || mGDConfig.getHeight() <= 0){
                        Bitmap bitmap = null;
                        try {
                            bitmap = GDBitmapUtil.bitmapDegree(mGDConfig.getmPath());
                        } catch (Exception e) {
                            e.printStackTrace();
                            bitmap = BitmapFactory.decodeFile(mGDConfig.getmPath());
                        }
                        Bitmap bitmapMin = null ;
                        try {
                            bitmapMin= new GDCompressUtil().SysCompressMin(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        garbage.add(bitmap);
                        garbage.add(bitmapMin);
                        if (new GDCompressUtil().compressLibJpeg(null==bitmapMin?bitmap:bitmapMin, mGDConfig.getSavePath())){
                            InformCallSuccess(mGDConfig.getSavePath());
                        }else {
                            InformCallError(0, "Image compression failure!");
                        }
                    }else {
                        Bitmap bitmap = null;
                        try {
                            bitmap = GDBitmapUtil.bitmapDegree(mGDConfig.getmPath());
                        } catch (Exception e) {
                            e.printStackTrace();
                            bitmap = BitmapFactory.decodeFile(mGDConfig.getmPath());
                        }
                        Bitmap bitmapMin = null ;
                        try {
                            bitmapMin= new GDCompressUtil().SysCompressMySamp(bitmap, mGDConfig.getWidth(), mGDConfig.getHeight());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        garbage.add(bitmap);
                        garbage.add(bitmapMin);
                        if (new GDCompressUtil().compressLibJpeg(null==bitmapMin?bitmap:bitmapMin, mGDConfig.getSavePath())){
                            InformCallSuccess(mGDConfig.getSavePath());
                        }else {
                            InformCallError(0, "Image compression failure!");
                        }
                    }
                }else {
                    Bitmap bitmap = null;
                    try {
                        bitmap = GDBitmapUtil.bitmapDegree(mGDConfig.getmPath());
                    } catch (Exception e) {
                        e.printStackTrace();
                        bitmap = BitmapFactory.decodeFile(mGDConfig.getmPath());
                    }
                    garbage.add(bitmap);
                    if (new GDCompressUtil().compressLibJpeg(bitmap, mGDConfig.getSavePath())){
                        InformCallSuccess(mGDConfig.getSavePath());
                    }else {
                        InformCallError(0, "Image compression failure!");
                    }
                }
            }
        }).start();
    }

    private void InformCallSuccess(final String path){
        try {
            GDBitmapUtil.saveBitmapDegree(path);
        }catch (Exception e){e.printStackTrace();}
        try {
            if (null!=garbage && garbage.size()>0){
                for (int i = 0; i < garbage.size(); i++) {
                    Bitmap bitmap = garbage.get(i);
                    if(bitmap != null && !bitmap.isRecycled()){
                        bitmap.recycle();
                        bitmap = null;
                    }
                }
                System.gc();
            }
        }catch (Exception e){e.printStackTrace();}
        try {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (null!= mGDCompressImageListener) mGDCompressImageListener.OnSuccess(path);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            if (null!= mGDCompressImageListener) mGDCompressImageListener.OnSuccess(path);
        }
    }
    private void InformCallError(final int code, final String errorMsg){
        try {
            if (null!=garbage && garbage.size()>0){
                for (int i = 0; i < garbage.size(); i++) {
                    Bitmap bitmap = garbage.get(i);
                    if(bitmap != null && !bitmap.isRecycled()){
                        bitmap.recycle();
                        bitmap = null;
                    }
                }
                System.gc();
            }
        }catch (Exception e){e.printStackTrace();}
        try {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (null!= mGDCompressImageListener) mGDCompressImageListener.OnError(code, errorMsg);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            if (null!= mGDCompressImageListener) mGDCompressImageListener.OnError(code, errorMsg);
        }
    }

    private void initSavePath(){
        if (null==mGDConfig.getSavePath() || TextUtils.equals("", mGDConfig.getSavePath())){
            mGDConfig.setSavePath(mGDConfig.getmPath());
        }
    }


}
