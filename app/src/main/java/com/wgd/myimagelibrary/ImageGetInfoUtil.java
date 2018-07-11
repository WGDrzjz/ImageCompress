package com.wgd.myimagelibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;

public class ImageGetInfoUtil {

    public ImageInfoBean getImageInfo(Bitmap bitmap){
        ImageInfoBean imageInfoBean = new ImageInfoBean();
        try {
            int[] originSize = new int[2];
            originSize[0] = bitmap.getWidth();
            originSize[1] = bitmap.getHeight();
            String path = Environment.getExternalStorageDirectory() + "/imageinfo/cache01.jpg" ;
            imageInfoBean =  new ImageInfoBean(originSize, "", new File(path).length() >> 10);
        }catch (Exception e){e.printStackTrace();}
        return imageInfoBean;
    }

    public ImageInfoBean getImageInfo(String path){
        int[] originSize = computeSize(path);
        return new ImageInfoBean(originSize, path, new File(path).length() >> 10);
    }

    private int[] computeSize(String srcImg) {
        int[] size = new int[2];

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;

        BitmapFactory.decodeFile(srcImg, options);
        size[0] = options.outWidth;
        size[1] = options.outHeight;

        return size;
    }

}
