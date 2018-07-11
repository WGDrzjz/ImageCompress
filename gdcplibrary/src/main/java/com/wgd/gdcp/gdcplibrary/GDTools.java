package com.wgd.gdcp.gdcplibrary;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class GDTools {

    /*
    * 以720*1280为标准，计算图片宽高
    * */
    public static int[] getWH(int width, int height){
        int whSize[] = new int[2];
        double whb = (double) width / (double) height;
        double finwhb = 1280.0 / 720.0;
        int newwidth = 1280;
        int newheight = 720;
        if (whb > finwhb){//以高度为标准
            newwidth = (int) (720 * whb);
        }else if (whb < finwhb){//以宽度为标准
            newheight = (int) (1280 / whb);
        }
        whSize[0] = newwidth;
        whSize[1] = newheight;
        return whSize;
    }
    /*
     * 以实际设备宽高为标准，计算图片宽高
     * */
    public static int[] getDevWH(Context context){
        int whSize[] = new int[2];

        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density = dm.density;
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        double whb = (double) width / (double) height;
//        double finwhb = 720.0 / 1280.0;
        double finwhb = 1280.0 / 720.0;
        int newwidth = 1280;
        int newheight = 720;
        if (whb > finwhb){//以高度为标准
            newwidth = (int) (720 * whb);
        }else if (whb < finwhb){//以宽度为标准
            newheight = (int) (1280 / whb);
        }
        whSize[0] = newwidth;
        whSize[1] = newheight;

        return whSize;
    }

    public static int getSampWHforImage(int imageWidth, int imageHeight, int width, int height){
        double whbIMG = (double) imageWidth / (double) imageHeight ;
        double whb = (double) width / (double) height ;
        int a = 1 ;
        if (whbIMG >= whb){
            a = imageWidth / width ;
        }else {
            a = imageHeight / height ;
        }
        if (a%2 == 1)a = a+1;
        return a ;
    }

}
