package com.asysbang.input.tess;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

public class TessHelper {

    public static TessHelper getInstance() {
        return InstanceHolder.sInstance;
    }

    private TessHelper() {

    }

    TessBaseAPI mApi;



    public void init() {
        mApi = new TessBaseAPI();
        mApi.init("/sdcard/tess","chi_sim");
        Log.e("","=====init");
        // just demo
//        Bitmap bitmap = BitmapFactory.decodeFile("");
//        api.setImage(bitmap);
//        api.getUTF8Text();
        // === over demo

//        test();
    }

    public String getTextFromBitmap(Bitmap bitmap) {
        mApi.setImage(bitmap);
        return mApi.getUTF8Text();
    }

    public void test() {
        Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/test_xing.png");
        Log.e("","========mApi="+mApi);
        Log.e("","========bitmap="+bitmap);
        mApi.setImage(bitmap);
        String utf8Text = mApi.getUTF8Text();
        Log.e("","=====test res = "+utf8Text);
    }


    private static final class InstanceHolder {
        private static TessHelper sInstance = new TessHelper();
    }


}