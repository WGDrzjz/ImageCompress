package com.wgd.myimagelibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class BitmapFromUriUtil {

    public static Bitmap getBitmapFromUri(Context context, Uri uri)
   {
           try
           {
                // 读取uri所在的图片
               Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                return bitmap;
               }
           catch (Exception e)
           {
                Log.e("[Android]", e.getMessage());
                Log.e("[Android]", "目录为：" + uri);
                e.printStackTrace();
                return null;
               }
          }

}
