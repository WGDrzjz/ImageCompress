package com.wgd.gdcp.gdcplibrary;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

public class GDCompress {

    private Context mContext;
    private GDCompressImageListener mGDCompressImageListener;
    private String mPath ;
    private String savePath ;

    private Bitmap bitmap ;

    public GDCompress(Context context, String path, String savepath, GDCompressImageListener GDCompressImageListener){
        this.mContext = context ;
        this.mPath = path ;
        this.savePath = savepath ;
        this.mGDCompressImageListener = GDCompressImageListener;

        startCompress();
    }

    private void startCompress(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (compressLibJpeg(mPath)){
                    InformCallSuccess(savePath);
                }else {
                    InformCallError(0, "Image compression failure!");
                }
            }
        }).start();
    }

    private void InformCallSuccess(final String path){
        try {
            GDBitmapUtil.saveBitmapDegree(path);
        }catch (Exception e){e.printStackTrace();}
        try {
            if(bitmap != null && !bitmap.isRecycled()){
                bitmap.recycle();
                bitmap = null;
            }
            System.gc();
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

    private boolean compressLibJpeg(Bitmap bitmap, int quality){
        try {
            if (null==savePath || TextUtils.equals("", savePath)){
                savePath = mPath;
            }
            if (null==bitmap)return false;
            boolean codeString = ImageUtils.compressBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), savePath, quality);
            return codeString;
        }catch (Exception e){e.printStackTrace();}
        return false;
    }
    /*
     * path 原图路径
     * */
    private boolean compressLibJpeg(String path){
        try {
            bitmap = BitmapFactory.decodeFile(path);
            return compressLibJpeg(bitmap, 20);
        }catch (Exception e){e.printStackTrace();}
        return false;
    }
    /*
     * path 原图路径
     * */
    private boolean compressLibJpeg(String path, int quality){
        try {
            bitmap = BitmapFactory.decodeFile(path);
            return compressLibJpeg(bitmap, quality);
        }catch (Exception e){e.printStackTrace();}
        return false;
    }


}
