package com.wgd.gdcp.gdcplibrary;

import android.graphics.Bitmap;

/**
 *
 *
 * @author KINCAI
 *
 */
public class ImageUtils {
	static {
//		System.loadLibrary("jpeg");// libjpeg
		System.loadLibrary("gdimage");//
	}


//	public static native String compressBitmap(Bitmap bitmap, int width,
//                                               int height, int quality, byte[] fileName, boolean optimize);
	public static native boolean compressBitmap(Bitmap bitmap, int width, int height, String fileName, int quality);

}
