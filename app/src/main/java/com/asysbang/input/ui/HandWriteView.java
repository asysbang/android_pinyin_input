package com.asysbang.input.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.asysbang.input.tess.TessHelper;

import java.io.File;
import java.io.FileOutputStream;

public class HandWriteView extends View {

    Paint mPaint;
    private MaskFilter mEmboss;
    private MaskFilter mBlur;

    private static final float MINP = 0.25f;
    private static final float MAXP = 0.75f;

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;

    public HandWriteView(Context context) {
        super(context);

    }

    public HandWriteView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mPaint != null) {
            canvas.drawColor(0xFFFFFFFF);
//记录之前的笔画
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
//            saveBitmap();
//画最近的一个笔画
            canvas.drawPath(mPath, mPaint);

//            canvas.drawColor(0xFFFFFFFF);
//            canvas.drawPath(mPath, mPaint);
        }
    }

    public void clear() {
        Log.e("", "=============clear");
        mPath.reset();
        mCanvas.drawColor(0xFFFFFFFF);

    }

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void saveBitmap() {
        try {
            File file = new File("/sdcard/tess_"+System.currentTimeMillis()+".png");
            FileOutputStream out = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        // kill this so we don't double draw
//        saveBitmap();
        String res = TessHelper.getInstance().getTextFromBitmap(mBitmap);
        Log.e("","======="+res);
        mPath.reset();

    }


    public void init() {
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mBitmap = Bitmap.createBitmap(1280, 600, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(0xFFFFFFFF);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFF000000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
        mEmboss = new EmbossMaskFilter(new float[]{1, 1, 1}, 0.4f, 6, 3.5f);
        mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }
}


