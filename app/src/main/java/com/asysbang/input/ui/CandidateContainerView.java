package com.asysbang.input.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asysbang.input.R;
import com.asysbang.input.service.InputCoreService;

import java.util.List;

public class CandidateContainerView extends LinearLayout implements View.OnClickListener {

    private Button mMoreInputMethod;

    private TextView mTextView;

    private CandidateView mCandidateView;

    private InputCoreService mInputCoreService;

    private RelativeLayout mSettingsContainer;


    public CandidateContainerView(Context context, AttributeSet attrs) {
        super(context,attrs);

    }

    public void init(){
        mSettingsContainer = findViewById(R.id.candidate_container);
        mMoreInputMethod = findViewById(R.id.more_input_method);
        mTextView =findViewById(R.id.candidate_text);
        mCandidateView = findViewById(R.id.candidate_view);
        mCandidateView.init();
        mMoreInputMethod.setOnClickListener(this);
    }

    public void setInputCoreService(InputCoreService service) {
        mInputCoreService = service;
        mCandidateView.setService(service);
    }


    @Override
    public void onClick(View v) {
        Log.e("","=====================");
        mInputCoreService.switchMoreInputMethodView();
//        mInputCoreService.showMoreInputMethodView(v);
    }

    public void setSuggestions(List<String> suggestions, boolean completions, boolean typedWordValid) {
        mSettingsContainer.setVisibility(View.GONE);
        mCandidateView.setVisibility(View.VISIBLE);
        mCandidateView.setSuggestions(suggestions, completions, typedWordValid);
    }

    public void clear() {
        mCandidateView.clear();
        mSettingsContainer.setVisibility(View.VISIBLE);
        mCandidateView.setVisibility(View.GONE);
    }
}
