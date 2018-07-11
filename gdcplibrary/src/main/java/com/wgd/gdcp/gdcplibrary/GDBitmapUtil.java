package com.wgd.gdcp.gdcplibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GDBitmapUtil {

    public static int[] getBitmapWH(String path){
        int whSize[] = new int[2];
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        //设置为true,表示解析Bitmap对象，该对象不占内存
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

    public static Bitmap bitmapDegree(String path) throws Exception{
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        try {
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
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        //设置为true,表示解析Bitmap对象，该对象不占内存
//        options.inJustDecodeBounds = true;
//        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

//        return Bitmap.createBitmap(bitmap, 0, 0, options.outWidth, options.outHeight, matrix, true);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }catch (Exception e){e.printStackTrace();}
        return bitmap ;

    }

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
                //设置为true,表示解析Bitmap对象，该对象不占内存
                options.inJustDecodeBounds = true;
                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                File saveBitmapFile = saveBitmapFile(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true), path);
            }
        }catch (Exception e){e.printStackTrace();}

    }


}
