package com.wgd.gdcp.gdcplibrary;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.wgd.gdcp.gdcplibrary.thread.ThreadManager;

import java.util.ArrayList;
import java.util.List;

/*
* Compression of Multiple Pictures
*
* Please apply for permission dynamically
* android.permission.WRITE_EXTERNAL_STORAGE
* android.permission.READ_EXTERNAL_STORAGE
*
* */
public class GDCompressImageS {

    private Context mContext;
    private GDCompressImageSListener mGDCompressImageSListener;
    private List<GDImageBean> imageBeanList;
    List<GDImageBean> newImageBean = new ArrayList<>();
    private boolean isHaveFail = false ;
    private List<Bitmap> garbage = new ArrayList<>();

    public GDCompressImageS(Context context, List<GDImageBean> imageBeanList, GDCompressImageSListener GDCompressImageSListener){
        this.mContext = context ;
        this.mGDCompressImageSListener = GDCompressImageSListener;
        this.imageBeanList = imageBeanList ;
        startCompressSA();
//        startCompressS();
//        startCompress();
    }

    //GDCompressA
    private void startCompressSA(){
        if (null!=imageBeanList && imageBeanList.size() >0) {
            newImageBean.clear();
            isHaveFail = false;
            GDCompressA gdCompressA = new GDCompressA(mContext, new GDCompressImageListenerA() {
                @Override
                public void OnSuccess(GDImageBean gdImageBean) {
                    Log.i("GDCimage", "OnSuccess: ========0002========");
                    InformCallFinish(gdImageBean);
                }

                @Override
                public void OnError(GDImageBean gdImageBean) {
                    Log.i("GDCimage", "OnError: ========0003========");
                    InformCallFinish(gdImageBean);
                }
            });
            for (int i = 0; i < imageBeanList.size(); i++) {
                final GDImageBean imageBeana = imageBeanList.get(i);
                gdCompressA.start(imageBeana);
            }
        }else {
            if (null!= mGDCompressImageSListener) mGDCompressImageSListener.OnError(newImageBean);
        }
    }
    private void startCompressS(){
        if (null!=imageBeanList && imageBeanList.size() >0){
            newImageBean.clear();
            isHaveFail = false ;

            for ( int i = 0; i < imageBeanList.size(); i++) {
                final GDImageBean imageBeana = imageBeanList.get(i);
                Log.i("GDCimage", "startCompressS: ========0001========" + i);
                new GDCompressC(mContext,
                        imageBeana.getmGDConfig(), new GDCompressImageListener() {
                    @Override
                    public void OnSuccess(String path) {
                        Log.i("GDCimage", "OnSuccess: ========0002========");
                        GDImageBean imageBean = imageBeana;
                        imageBean.setCode(0);
                        InformCallFinish(imageBean);
                    }

                    @Override
                    public void OnError(int code, String errorMsg) {
                        Log.i("GDCimage", "OnError: ========0003========");
                        GDImageBean imageBean = imageBeana;
                        isHaveFail = true ;
                        imageBean.setCode(1);
                        imageBean.setErrorMsg("Image compression failure!");
                        InformCallFinish(imageBean);
                    }
                });
            }
        }else {
            if (null!= mGDCompressImageSListener) mGDCompressImageSListener.OnError(newImageBean);
        }
    }
    private void startCompress(){
        Log.i("GDCimage", "startCompress: ======imageBeanList.size()=======" + imageBeanList.size());
        if (null!=imageBeanList && imageBeanList.size() >0){
                    newImageBean.clear();
                    isHaveFail = false ;
                    for ( int i = 0; i < imageBeanList.size(); i++) {
                        final GDImageBean imageBeana = imageBeanList.get(i);

                        ThreadManager.getIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                Bitmap bitmapMin = null;
                                String mPath = imageBeana.getmGDConfig().getmPath();
                                GDImageBean imageBean = imageBeana ;
                                if (!GDTools.ImageTesting(mPath)){
//                            InformCallError(1, "Incorrect picture format!");
                                    imageBean.setCode(1);
                                    imageBean.setErrorMsg("Image compression failure!");
                                    InformCallFinish(imageBean);

//                                    continue;
                                }else {
                                    String savePath = imageBeana.getmGDConfig().getSavePath();

                                    if (null==savePath|| TextUtils.isEmpty(savePath)){
                                        GDConfig gdConfig = imageBeana.getmGDConfig();
                                        gdConfig.setSavePath(mPath);
                                        imageBean.setmGDConfig(gdConfig);
                                    }

                                    GDConfig mGDConfig = imageBean.getmGDConfig();

                                    if (mGDConfig.isChangeWH()){
                                        if (mGDConfig.getWidth() <=0 || mGDConfig.getHeight() <= 0){
                                            try {
                                                bitmapMin = new GDCompressUtil().SysCompressMin(mGDConfig.getmPath());
//                            bitmapMin= new GDCompressUtil().SysCompressMin(bitmap);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            if (null==bitmapMin) Log.i("GDCimage", "startCompress: ====== null==bitmapMin =======" );
                                            garbage.add(bitmapMin);
                                            if (null== bitmapMin){
                                                isHaveFail = true ;
                                                imageBean.setCode(1);
                                                imageBean.setErrorMsg("Image compression failure!");
                                            }else {
                                                if (new GDCompressUtil().compressLibJpeg(bitmapMin, mGDConfig.getSavePath())) {
                                                    imageBean.setCode(0);
                                                } else {
                                                    isHaveFail = true ;
                                                    imageBean.setCode(1);
                                                    imageBean.setErrorMsg("Image compression failure!");
                                                }
                                            }

                                            InformCallFinish(imageBean);

                                        }else {
                                            try {
                                                bitmapMin = new GDCompressUtil().SysCompressMin(mGDConfig.getmPath());
//                            bitmapMin= new GDCompressUtil().SysCompressMin(bitmap);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            garbage.add(bitmapMin);
                                            if (null== bitmapMin){
                                                isHaveFail = true ;
                                                imageBean.setCode(1);
                                                imageBean.setErrorMsg("Image compression failure!");
                                            }else {
                                                if (new GDCompressUtil().compressLibJpeg(bitmapMin, mGDConfig.getSavePath())) {
                                                    imageBean.setCode(0);
                                                } else {
                                                    isHaveFail = true ;
                                                    imageBean.setCode(1);
                                                    imageBean.setErrorMsg("Image compression failure!");
                                                }
                                            }

                                            InformCallFinish(imageBean);
                                        }
                                    }else {
                                        try {
                                            bitmapMin = GDBitmapUtil.getBitmap(mGDConfig.getmPath());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            bitmapMin = BitmapFactory.decodeFile(mGDConfig.getmPath());
                                        }
                                        if (null==bitmapMin) Log.i("GDCimage", "startCompress: ====  else  == null==bitmapMin =======" );
                                        garbage.add(bitmapMin);
                                        if (new GDCompressUtil().compressLibJpeg(bitmapMin, mGDConfig.getSavePath())){
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
                        });
                }

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
