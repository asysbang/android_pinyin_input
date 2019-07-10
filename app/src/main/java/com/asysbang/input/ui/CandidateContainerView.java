package com.asysbang.input.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.asysbang.input.R;
import com.asysbang.input.service.InputCoreService;

import java.util.List;

public class CandidateContainerView extends LinearLayout implements View.OnClickListener {

    private Button mMoreInputMethod;
    private Button mHWView;

    private TextView mTextView;

    private CandidateView mCandidateView;

    private InputCoreService mInputCoreService;

    private RelativeLayout mSettingsContainer;

    //两个子view 用switcher 方便控制管理
    private ViewSwitcher mSwitcher;


    public CandidateContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void init() {
        Log.e("", "====================CandidateContainerView=init");
        mSettingsContainer = findViewById(R.id.candidate_container);
        mMoreInputMethod = findViewById(R.id.more_input_method);
        mTextView = findViewById(R.id.candidate_text);
        mCandidateView = findViewById(R.id.candidate_view);
        mCandidateView.init();
        mMoreInputMethod.setOnClickListener(this);
        mSwitcher = findViewById(R.id.candidate_switcher);
        mHWView = findViewById(R.id.show_hw_view);
        mHWView.setOnClickListener(this);
    }

    public void setInputCoreService(InputCoreService service) {
        mInputCoreService = service;
        mCandidateView.setService(service);
    }


    private PopupWindow mPopView;

    @Override
    public void onClick(View v) {
        Log.e("", "====================onClick=");
        switch (v.getId()){
            case R.id.more_input_method:
                break;
            case R.id.show_hw_view:
                mInputCoreService.showInputView();
                break;
        }
//        mSwitcher.showNext();

//        mInputCoreService.switchMoreInputMethodView();
//        mInputCoreService.showMoreInputMethodView(v);
    }

    public void setSuggestions(List<String> suggestions, boolean completions, boolean typedWordValid) {
        Log.e("", "=====================setSuggestions = " + suggestions);
        if (null == suggestions) {
            mSettingsContainer.setVisibility(View.VISIBLE);
            mCandidateView.setVisibility(View.GONE);
        } else {
            mSettingsContainer.setVisibility(View.GONE);
            mCandidateView.setVisibility(View.VISIBLE);
            mCandidateView.setSuggestions(suggestions, completions, typedWordValid);
        }

    }

    public void clear() {
        mCandidateView.clear();
    }
}
