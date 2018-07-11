package com.wgd.gdcp.gdcplibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class GDCompressUtil {

    private int computeSize(int srcWidth, int srcHeight) {
        srcWidth = srcWidth % 2 == 1 ? srcWidth + 1 : srcWidth;
        srcHeight = srcHeight % 2 == 1 ? srcHeight + 1 : srcHeight;

        int longSide = Math.max(srcWidth, srcHeight);
        int shortSide = Math.min(srcWidth, srcHeight);

        float scale = ((float) shortSide / longSide);
        if (scale <= 1 && scale > 0.5625) {
            if (longSide < 1664) {
                return 1;
            } else if (longSide >= 1664 && longSide < 4990) {
                return 2;
            } else if (longSide > 4990 && longSide < 10240) {
                return 4;
            } else {
                return longSide / 1280 == 0 ? 1 : longSide / 1280;
            }
        } else if (scale <= 0.5625 && scale > 0.5) {
            return longSide / 1280 == 0 ? 1 : longSide / 1280;
        } else {
            return (int) Math.ceil(longSide / (1280.0 / scale));
        }
    }

    /*
     * 系统压缩，图片大小会改变，以1280为基准
     */
    public Bitmap SysCompressMin(Bitmap bitmap) throws Exception {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = computeSize(bitmap.getWidth(), bitmap.getHeight());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Bitmap tagBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
        return tagBitmap;
    }
    /*
     * 系统压缩，图片大小会改变，
     */
    public Bitmap SysCompressMySamp(Bitmap bitmap, int width, int height) throws Exception {
        BitmapFactory.Options options = new BitmapFactory.Options();
        int samp = GDTools.getSampWHforImage(bitmap.getWidth(), bitmap.getHeight(), width, height);
        options.inSampleSize = samp;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Bitmap tagBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
        return tagBitmap;
    }


    public boolean compressLibJpeg(Bitmap bitmap, String savePath){
        try {
            if (null==bitmap)return false;
            return compressLibJpeg(bitmap, savePath, 20);
        }catch (Exception e){e.printStackTrace();}
        return false;
    }

    public boolean compressLibJpeg(Bitmap bitmap, String savePath, int quality){
        try {
            if (null==bitmap)return false;
            boolean codeString = ImageUtils.compressBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), savePath, quality);
            return codeString;
        }catch (Exception e){e.printStackTrace();}
        return false;
    }
    /*
     * path 原图路径
     * */
    public boolean compressLibJpeg(String path, String savePath){
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            return compressLibJpeg(bitmap, savePath, 20);
        }catch (Exception e){e.printStackTrace();}
        return false;
    }
    /*
     * path 原图路径
     * */
    public boolean compressLibJpeg(String path, String savePath, int quality){
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            return compressLibJpeg(bitmap, savePath, quality);
        }catch (Exception e){e.printStackTrace();}
        return false;
    }



}
