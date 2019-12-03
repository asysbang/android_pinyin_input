package com.asysbang.input.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.util.Log;

public class MyKeyBoardView extends KeyboardView {
    public MyKeyBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyKeyBoardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    static final int KEYCODE_OPTIONS = -100;

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("","==========onDraw==");
    }

    @Override
    protected boolean onLongPress(Keyboard.Key key) {
        Log.e("","==========onLongPress=="+key.label);
        if (key.codes[0] == Keyboard.KEYCODE_CANCEL) {
            getOnKeyboardActionListener().onKey(KEYCODE_OPTIONS, null);
            return true;
        } else {
            return super.onLongPress(key);
        }
    }

}
