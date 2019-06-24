package com.asysbang.input.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.asysbang.input.R;

public class KeyBoardContainerView extends LinearLayout {

    private RelativeLayout mMoreInputMethodView;

    private MyKeyBoardView mMyKeyBoardView;

    private boolean isMoreInputMethodViewShowing = false;

    private MoreInputMethodPopupWindow mMoreInputMethodPopupWindow;

    public KeyBoardContainerView(Context context) {
        super(context);
    }

    public KeyBoardContainerView(Context context , AttributeSet attrs) {
        super(context, attrs);
    }


    public void init() {
        isMoreInputMethodViewShowing = false;
        mMoreInputMethodView = findViewById(R.id.keyboard_container);
        mMyKeyBoardView = findViewById(R.id.keyboard);
        mMoreInputMethodPopupWindow = new MoreInputMethodPopupWindow(getContext());


    }


    /**
     * more input method view 弹出状态应该谁来记录？？？？
     *
     *
     */
    public void showMoreInputMethodView(View anchor) {
        mMoreInputMethodPopupWindow.showAsDropDown(anchor);
    }

    public void hideMoreInputMethodView () {

    }



    /**
     * 弹出更多输入法选择界面
     *
     * 此处用popupwindow效果更好，容易控制高度
     *
     */

    public void switchMoreInputMethodView() {
        Log.e("","==================KeyBoardContainerView switchMoreInputMethodView");

        if (isMoreInputMethodViewShowing) {
            mMoreInputMethodView.setVisibility(View.GONE);
            mMyKeyBoardView.setVisibility(View.VISIBLE);
            isMoreInputMethodViewShowing = false;
            Log.e("","==============1111=");
        } else {
            mMoreInputMethodView.setVisibility(View.VISIBLE);
            mMyKeyBoardView.setVisibility(View.GONE);
            isMoreInputMethodViewShowing = true;
            Log.e("","==============22222=");
        }
    }


    class MoreInputMethodPopupWindow extends PopupWindow{

        public MoreInputMethodPopupWindow(Context context) {
            super(context);
            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            setOutsideTouchable(true);
            setFocusable(true);
            setBackgroundDrawable(new ColorDrawable(Color.BLUE));
            View contentView = LayoutInflater.from(context).inflate(R.layout.popup_more_input_method,null, false);
            setContentView(contentView);
        }
    }
}
