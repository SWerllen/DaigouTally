package com.example.andriod.daigoutally;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapThumb {
    private static int calculateInSampleSize(BitmapFactory.Options option,int reqWidth,int reqHeight){
        final int height = option.outHeight;
        final int width = option.outWidth;
        int inSampleSize = 1;
        if(height>reqHeight||width>reqWidth){
            final int halfHeight = height/2;
            final int halfWidth = width/2;
            while((halfHeight/inSampleSize)>reqHeight&&(halfWidth/inSampleSize)>reqWidth){
                inSampleSize*=2;
            }
        }
        return inSampleSize;
    }
    private static Bitmap createScaleBitmap(Bitmap src, int dstWidth,int dstHeight) {
        Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
        if (src != dst) { // 如果没有缩放，那么不回收
            src.recycle(); // 释放Bitmap的native像素数组
        }
        return dst;
    }
    public static Bitmap decodeSampledBitmapFromFd(String pathName,int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeFile(pathName, options);
        return createScaleBitmap(src, reqWidth, reqHeight);
    }
    public static Bitmap decodeSampledBitmapFromFd(String pathName,int reqWidth) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        int reqHeight=options.outHeight*reqWidth/options.outWidth;
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeFile(pathName, options);
        return createScaleBitmap(src, reqWidth, reqHeight);
    }
    public static Bitmap decodeSampledBitmapFromFdByHeight(String pathName,int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        int reqWidth=options.outWidth*reqHeight/options.outHeight;
        if(reqWidth< 200){
            reqWidth=200;
            reqHeight=options.outHeight*reqWidth/options.outWidth;
        }
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeFile(pathName, options);
        return createScaleBitmap(src, reqWidth, reqHeight);
    }
}

