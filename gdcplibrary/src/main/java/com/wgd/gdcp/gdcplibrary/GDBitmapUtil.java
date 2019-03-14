package com.wgd.gdcp.gdcplibrary;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GDBitmapUtil {

    public static int[] getBitmapWH(String path){
        int whSize[] = new int[2];
//        BitmapFactory.Options options = new BitmapFactory.Options();
//
//        options.inJustDecodeBounds = true;
//        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
//        whSize[0] = bitmap.getWidth();
//        whSize[1] = bitmap.getHeight();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;

        BitmapFactory.decodeFile(path, options);
        whSize[0] = options.outWidth;
        whSize[1] = options.outHeight;

        return whSize;
    }

    public static File saveBitmapFile(Bitmap bitmap, String filepath){
        File file=new File(filepath);//
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void saveBitmapFileVo(Bitmap bitmap, String filepath) throws Exception{
        File file=new File(filepath);//
        if (!file.exists()){
            file = new File(filepath);
        }
//        try {
//            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//            bos.flush();
//            bos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
    }

//    public static Bitmap bitmapDegree(String path) throws Exception{
//        return bitmapDegree(null,path);
//    }
    public static Bitmap bitmapDegree(Context mContext, String path) throws Exception{
        Bitmap bitmap = null ;
        try {
            bitmap = BitmapFactory.decodeFile(path);
        }catch (Exception e){
            bitmap = adjustImage(mContext, path, 2);
        }
        boolean isRotate = false ;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            Matrix matrix = new Matrix();
            int angle = 0;
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    angle = 90;
                    isRotate = true ;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    angle = 180;
                    isRotate = true ;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    angle = 270;
                    isRotate = true ;
                    break;
            }
            matrix.postRotate(angle);
//        BitmapFactory.Options options = new BitmapFactory.Options();
//
//        options.inJustDecodeBounds = true;
//        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

//        return Bitmap.createBitmap(bitmap, 0, 0, options.outWidth, options.outHeight, matrix, true);
            if (isRotate)return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            else return bitmap ;
        }catch (Exception e){e.printStackTrace();}
        return bitmap ;

    }

    private static Bitmap adjustImage(Context mContext, String absolutePath, int size) {
        Bitmap bm = null ;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        //
        opt.inJustDecodeBounds = true;
//        bm = BitmapFactory.decodeFile(absolutePath, opt);

        //
        int picWidth = opt.outWidth;
        int picHeight = opt.outHeight;

        //
        WindowManager windowManager = ((Activity)mContext).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();

        //
        opt.inSampleSize = 1;
        //
        if (picWidth > picHeight) {
            if (picWidth > screenWidth)
                opt.inSampleSize = picWidth / screenWidth;
        } else {
            if (picHeight > screenHeight)
                opt.inSampleSize = picHeight / screenHeight;
        }

        if (opt.inSampleSize<=1)opt.inSampleSize = size ;
        //
        opt.inJustDecodeBounds = false;
        try {
            bm= BitmapFactory.decodeFile(absolutePath, opt);
        }catch (Exception e){
            bm = adjustImage(mContext, absolutePath, size+1);
        }
        return bm ;
    }
//:gdcplibrary:bintrayUpload
    public static void saveBitmapDegree(String path){
        try {
            //
            ExifInterface exifInterface = new ExifInterface(path);
            Matrix matrix = new Matrix();
            int angle = 0;
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    angle = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    angle = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    angle = 270;
                    break;
            }
            matrix.postRotate(angle);
            if (angle != 0){
                BitmapFactory.Options options = new BitmapFactory.Options();
                //
                options.inJustDecodeBounds = true;
                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                File saveBitmapFile = saveBitmapFile(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true), path);
            }
        }catch (Exception e){e.printStackTrace();}

    }

    public static Bitmap getBitmap(String path){
        try {
            return BitmapFactory.decodeFile(path);

        }catch (Exception e){e.printStackTrace();}
        Log.i("GDCimage", "getBitmap: ==============02==============");
        BitmapFactory.Options options = new BitmapFactory.Options();
        //
//                options.inJustDecodeBounds = true;
        options.inSampleSize = 2 ;
        Log.i("GDCimage", "getBitmap: ==============01==============");
        return BitmapFactory.decodeFile(path, options);
    }


}
