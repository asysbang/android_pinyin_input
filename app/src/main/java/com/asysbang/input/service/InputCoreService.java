package com.asysbang.input.service;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.asysbang.input.R;
import com.asysbang.input.ui.CandidateContainerView;
import com.asysbang.input.ui.CandidateView;
import com.asysbang.input.ui.KeyBoardContainerView;
import com.asysbang.input.ui.MyKeyBoard;
import com.asysbang.input.ui.MyKeyBoardView;

import java.util.ArrayList;
import java.util.List;

public class InputCoreService extends InputMethodService implements KeyboardView.OnKeyboardActionListener,InputCoreServiceInterface{

    private InputMethodManager mInputMethodManager;
    private String mWordSeparators;


    //键盘视图
    private MyKeyBoardView mInputView;
    private KeyBoardContainerView mKeyBoardContainerView;
    //提示布局视图
    private CandidateContainerView mCandidateContainerView;

    //qwerty 键盘
    private MyKeyBoard mQwertyKeyboard;
    //符号  键盘
    private MyKeyBoard mSymbolsKeyboard;
    //Shift 符号 键盘
    private MyKeyBoard mSymbolsShiftedKeyboard;


    private boolean mCompletionOn = false;
    private StringBuilder mComposing = new StringBuilder();
    private CompletionInfo[] mCompletions;
    private boolean mCapsLock;

    public static final String ACTION_SHOW_PICKER = "com.asysbang.input.show_picker";



    @Override
    public void onCreate() {
        super.onCreate();
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mWordSeparators = getResources().getString(R.string.word_separators);
    }


    @Override
    public void onInitializeInterface() {
        super.onInitializeInterface();
        mQwertyKeyboard = new MyKeyBoard(this, R.xml.qwerty);
        mSymbolsKeyboard = new MyKeyBoard(this, R.xml.symbols);
        mSymbolsShiftedKeyboard = new MyKeyBoard(this, R.xml.symbols_shift);
        //默认弹出 candidate view  ，无数据时显示settings view
        setCandidatesViewShown(true);
    }

    @Override
    public View onCreateInputView() {
        mKeyBoardContainerView = (KeyBoardContainerView) getLayoutInflater().inflate(R.layout.keyboard_container_view,null);
        mKeyBoardContainerView.init();
        mInputView = mKeyBoardContainerView.findViewById(R.id.keyboard);
        mInputView.setOnKeyboardActionListener(this);
        mInputView.setKeyboard(mQwertyKeyboard);
        return mKeyBoardContainerView;
    }

    @Override
    public View onCreateCandidatesView() {
        mCandidateContainerView= (CandidateContainerView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.candidate_container_view, null);
        mCandidateContainerView.init();
        mCandidateContainerView.setInputCoreService(this);
        return mCandidateContainerView;
    }

    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);
        mComposing.setLength(0);
        updateCandidates();
        mCompletionOn = false;
        mCompletions = null;
        Log.i("=====", "===========onStartInput==");
    }

    @Override
    public void onFinishInput() {
        super.onFinishInput();
        // Clear current composing text and candidates.
        mComposing.setLength(0);
        updateCandidates();
        Log.i("=====", "===========onFinishInput==");
    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
        Log.i("=====", "===========onStartInputView==");
    }

    @Override
    public void onFinishInputView(boolean finishingInput) {
        super.onFinishInputView(finishingInput);
        Log.i("=====", "===========onInitializeInterface==");
    }


    @Override
    protected void onCurrentInputMethodSubtypeChanged(InputMethodSubtype newSubtype) {
        super.onCurrentInputMethodSubtypeChanged(newSubtype);
        Log.i("=====", "===========onCurrentInputMethodSubtypeChanged==");
    }

    @Override
    public void onUpdateSelection(int oldSelStart, int oldSelEnd, int newSelStart, int newSelEnd, int candidatesStart, int candidatesEnd) {
        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart, candidatesEnd);
        Log.i("=====", "===========onUpdateSelection==");
        // If the current selection in the text view changes, we should
        // clear whatever candidate text we have.
        if (mComposing.length() > 0 && (newSelStart != candidatesEnd
                || newSelEnd != candidatesEnd)) {
            mComposing.setLength(0);
            updateCandidates();
            InputConnection ic = getCurrentInputConnection();
            if (ic != null) {
                ic.finishComposingText();
            }
        }
    }


    @Override
    public void onDisplayCompletions(CompletionInfo[] completions) {
        super.onDisplayCompletions(completions);
        Log.i("=====", "===========onDisplayCompletions==");
        if (mCompletionOn) {
            mCompletions = completions;
            if (completions == null) {
                setSuggestions(null, false, false);
                return;
            }
            List<String> stringList = new ArrayList<String>();
            for (int i = 0; i < completions.length; i++) {
                CompletionInfo ci = completions[i];
                if (ci != null) stringList.add(ci.getText().toString());
            }
            setSuggestions(stringList, true, true);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPress(int primaryCode) {
        Log.i("=====", "===========onPress==");
    }

    @Override
    public void onRelease(int primaryCode) {
        Log.i("=====", "===========onRelease==");
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        Log.i("=====", "===========onKey==");
        if (isWordSeparator(primaryCode)) {
            // Handle separator
            if (mComposing.length() > 0) {
                commitTyped(getCurrentInputConnection());
            }
            sendKey(primaryCode);
        } else if (primaryCode == Keyboard.KEYCODE_DELETE) {
            handleBackspace();
        } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE
                && mInputView != null) {
            Keyboard current = mInputView.getKeyboard();
            if (current == mSymbolsKeyboard || current == mSymbolsShiftedKeyboard) {
                setLatinKeyboard(mQwertyKeyboard);
            } else {
                setLatinKeyboard(mSymbolsKeyboard);
                mSymbolsKeyboard.setShifted(false);
            }
        } else if (primaryCode == Keyboard.KEYCODE_SHIFT) {
            handleShift();
        } else {
            handleCharacter(primaryCode, keyCodes);
        }

    }

    /**
     * Helper function to commit any text being composed in to the editor.
     */
    private void commitTyped(InputConnection inputConnection) {
        if (mComposing.length() > 0) {
            inputConnection.commitText(mComposing, mComposing.length());
            mComposing.setLength(0);
            updateCandidates();
        }
    }

    /**
     * Update the list of available candidates from the current composing
     * text.  This will need to be filled in by however you are determining
     * candidates.
     */
    private void updateCandidates() {

        if (!mCompletionOn) {
            if (mComposing.length() > 0) {
                ArrayList<String> list = new ArrayList<String>();
                list.add(mComposing.toString());
                list.add(mComposing.toString()+"11");
                list.add(mComposing.toString()+"22");
                setSuggestions(list, true, true);
            } else {
                setSuggestions(null, false, false);
            }
        }
    }

    public void setSuggestions(List<String> suggestions, boolean completions,
                               boolean typedWordValid) {
        if (suggestions != null && suggestions.size() > 0) {
            setCandidatesViewShown(true);
        } else if (isExtractViewShown()) {
            setCandidatesViewShown(true);
        }
        if (mCandidateContainerView != null) {
            mCandidateContainerView.setSuggestions(suggestions, completions, typedWordValid);
        }
    }

    private void handleShift() {
        if (mInputView == null) {
            return;
        }

        Keyboard currentKeyboard = mInputView.getKeyboard();
        if (mQwertyKeyboard == currentKeyboard) {
            // Alphabet keyboard
//            checkToggleCapsLock();
            mInputView.setShifted(mCapsLock || !mInputView.isShifted());
        } else if (currentKeyboard == mSymbolsKeyboard) {
            mSymbolsKeyboard.setShifted(true);
            setLatinKeyboard(mSymbolsShiftedKeyboard);
            mSymbolsShiftedKeyboard.setShifted(true);
        } else if (currentKeyboard == mSymbolsShiftedKeyboard) {
            mSymbolsShiftedKeyboard.setShifted(false);
            setLatinKeyboard(mSymbolsKeyboard);
            mSymbolsKeyboard.setShifted(false);
        }
    }

    private IBinder getToken() {
        final Dialog dialog = getWindow();
        if (dialog == null) {
            return null;
        }
        final Window window = dialog.getWindow();
        if (window == null) {
            return null;
        }
        return window.getAttributes().token;
    }

    private void setLatinKeyboard(MyKeyBoard nextKeyboard) {
        final boolean shouldSupportLanguageSwitchKey =
                mInputMethodManager.shouldOfferSwitchingToNextInputMethod(getToken());
        nextKeyboard.setLanguageSwitchKeyVisibility(shouldSupportLanguageSwitchKey);
        mInputView.setKeyboard(nextKeyboard);
    }

    private void handleCharacter(int primaryCode, int[] keyCodes) {
        if (isInputViewShown()) {
            if (mInputView.isShifted()) {
                primaryCode = Character.toUpperCase(primaryCode);
            }
        }

        if (isAlphabet(primaryCode)) {
            mComposing.append((char) primaryCode);
            getCurrentInputConnection().setComposingText(mComposing, 1);
//            updateShiftKeyState(getCurrentInputEditorInfo());
            updateCandidates();
        } else {
            getCurrentInputConnection().commitText(
                    String.valueOf((char) primaryCode), 1);
        }

    }

    @Override
    public void onText(CharSequence text) {
        Log.i("=====", "===========onText==");
    }

    @Override
    public void swipeLeft() {
        Log.i("=====", "===========swipeLeft==");
    }

    @Override
    public void swipeRight() {
        Log.i("=====", "===========swipeRight==");
    }

    @Override
    public void swipeDown() {
        Log.i("=====", "===========swipeDown==");
    }

    @Override
    public void swipeUp() {
        Log.i("=====", "===========swipeUp==");
    }

    private String getWordSeparators() {
        return mWordSeparators;
    }

    public boolean isWordSeparator(int code) {
        String separators = getWordSeparators();
        return separators.contains(String.valueOf((char) code));
    }
    /**
     * Helper to determine if a given character code is alphabetic.
     */
    private boolean isAlphabet(int code) {
        if (Character.isLetter(code)) {
            return true;
        } else {
            return false;
        }
    }

    public void pickDefaultCandidate() {
        pickSuggestionManually(0);
    }

    public void pickSuggestionManually(String info) {
        if (mComposing.length() > 0) {
            // If we were generating candidate suggestions for the current
            // text, we would commit one of them here.  But for this sample,
            // we will just commit the current text.
            getCurrentInputConnection().commitText(info, info.length());
            mComposing.setLength(0);
            updateCandidates();
        }
    }

    public void pickSuggestionManually(int index) {
        if (mCompletionOn && mCompletions != null && index >= 0
                && index < mCompletions.length) {
            CompletionInfo ci = mCompletions[index];
            getCurrentInputConnection().commitCompletion(ci);
            if (mCandidateContainerView != null) {
                mCandidateContainerView.clear();
            }
//            updateShiftKeyState(getCurrentInputEditorInfo());
        } else if (mComposing.length() > 0) {
            // If we were generating candidate suggestions for the current
            // text, we would commit one of them here.  But for this sample,
            // we will just commit the current text.
            commitTyped(getCurrentInputConnection());
        }
    }


    private void handleBackspace() {
        final int length = mComposing.length();
        if (length > 1) {
            mComposing.delete(length - 1, length);
            getCurrentInputConnection().setComposingText(mComposing, 1);
            updateCandidates();
        } else if (length > 0) {
            mComposing.setLength(0);
            getCurrentInputConnection().commitText("", 0);
            updateCandidates();
        } else {
            keyDownUp(KeyEvent.KEYCODE_DEL);
        }
    }



    private void sendKey(int keyCode) {
        switch (keyCode) {
            case '\n':
                keyDownUp(KeyEvent.KEYCODE_ENTER);
                break;
            default:
                if (keyCode >= '0' && keyCode <= '9') {
                    keyDownUp(keyCode - '0' + KeyEvent.KEYCODE_0);
                } else {
                    getCurrentInputConnection().commitText(String.valueOf((char) keyCode), 1);
                }
                break;
        }
    }

    /**
     * Helper to send a key down / key up pair to the current editor.
     */
    private void keyDownUp(int keyEventCode) {
        getCurrentInputConnection().sendKeyEvent(
                new KeyEvent(KeyEvent.ACTION_DOWN, keyEventCode));
        getCurrentInputConnection().sendKeyEvent(
                new KeyEvent(KeyEvent.ACTION_UP, keyEventCode));
    }

    @Override
    public void switchMoreInputMethodView() {
        Log.e("","==================service switchMoreInputMethodView");
        mKeyBoardContainerView.switchMoreInputMethodView();
    }

    public void showMoreInputMethodView(View anchor) {
        mKeyBoardContainerView.showMoreInputMethodView(anchor);
    }
}
