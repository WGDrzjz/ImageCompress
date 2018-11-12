package com.wgd.gdcp.gdcplibrary;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class GDCompressImageS {

    private Context mContext;
    private GDCompressImageSListener mGDCompressImageSListener;
    private List<GDImageBean> imageBeanList;
    List<GDImageBean> newImageBean = new ArrayList<>();
    private boolean isHaveFail = false ;
    private List<Bitmap> garbage = new ArrayList<>();

    public GDCompressImageS(Context context, GDCompressImageSListener GDCompressImageSListener, List<GDImageBean> imageBeanList){
        this.mContext = context ;
        this.mGDCompressImageSListener = GDCompressImageSListener;
        this.imageBeanList = imageBeanList ;
        startCompress();
    }

    private void startCompress(){
        if (null!=imageBeanList && imageBeanList.size() >0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    newImageBean.clear();
                    isHaveFail = false ;
                    for (int i = 0; i < imageBeanList.size(); i++) {
                        GDImageBean imageBean = imageBeanList.get(i);
                        String mPath = imageBean.getmGDConfig().getmPath();
                        if (!GDTools.ImageTesting(mPath)){
//                            InformCallError(1, "Incorrect picture format!");
                            continue;
                        }
                        String savePath = imageBean.getmGDConfig().getSavePath();
                        if (null==savePath|| TextUtils.isEmpty(savePath)){
                            GDConfig gdConfig = imageBean.getmGDConfig();
                            gdConfig.setSavePath(mPath);
                            imageBean.setmGDConfig(gdConfig);
                        }

                        GDConfig mGDConfig = imageBean.getmGDConfig();

                        if (mGDConfig.isChangeWH()){
                            if (mGDConfig.getWidth() <=0 || mGDConfig.getHeight() <= 0){
                                Bitmap bitmap = null;
                                try {
                                    bitmap = GDBitmapUtil.bitmapDegree(mContext, mGDConfig.getmPath());
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
//                                    InformCallSuccess(mGDConfig.getSavePath());
                                    imageBean.setCode(0);
                                }else {
//                                    InformCallError(0, "Image compression failure!");
                                    isHaveFail = true ;
                                    imageBean.setCode(1);
                                    imageBean.setErrorMsg("Image compression failure!");
                                }
                                InformCallFinish(imageBean);
                            }else {
                                Bitmap bitmap = null;
                                try {
                                    bitmap = GDBitmapUtil.bitmapDegree(mContext, mGDConfig.getmPath());
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
                                        imageBean.setCode(0);
                                    }else {
                                        isHaveFail = true ;
                                        imageBean.setCode(1);
                                        imageBean.setErrorMsg("Image compression failure!");
                                    }
                                    InformCallFinish(imageBean);
                            }
                        }else {
                            Bitmap bitmap = null;
                            try {
                                bitmap = GDBitmapUtil.bitmapDegree(mContext, mGDConfig.getmPath());
                            } catch (Exception e) {
                                e.printStackTrace();
                                bitmap = BitmapFactory.decodeFile(mGDConfig.getmPath());
                            }
                            garbage.add(bitmap);
                            if (new GDCompressUtil().compressLibJpeg(bitmap, mGDConfig.getSavePath())){
                                imageBean.setCode(0);
                            }else {
                                isHaveFail = true ;
                                imageBean.setCode(1);
                                imageBean.setErrorMsg("Image compression failure!");
                            }
                            InformCallFinish(imageBean);
                        }

                    }
                }
            }).start();
        }else {
            if (null!= mGDCompressImageSListener) mGDCompressImageSListener.OnError(newImageBean);
        }
    }


    private void InformCallFinish(final GDImageBean imageBean){
        newImageBean.add(imageBean);
        try {
            GDBitmapUtil.saveBitmapDegree(imageBean.getmGDConfig().getSavePath());
        }catch (Exception e){e.printStackTrace();}
        if (newImageBean.size() >= imageBeanList.size()){
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
                        if (null!= mGDCompressImageSListener){
                            if (isHaveFail){
                                mGDCompressImageSListener.OnError(newImageBean);
                            }else {
                                mGDCompressImageSListener.OnSuccess(newImageBean);
                            }
                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
                if (null!= mGDCompressImageSListener){
                    if (isHaveFail){
                        mGDCompressImageSListener.OnError(newImageBean);
                    }else {
                        mGDCompressImageSListener.OnSuccess(newImageBean);
                    }
                }
            }
        }
    }

}
