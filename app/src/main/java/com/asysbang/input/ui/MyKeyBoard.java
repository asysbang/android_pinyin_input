package com.asysbang.input.ui;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.inputmethodservice.Keyboard;

import com.asysbang.input.R;


public class MyKeyBoard extends Keyboard {
    public MyKeyBoard(Context context, int xmlLayoutResId) {
        super(context, xmlLayoutResId);
    }

    public MyKeyBoard(Context context, int layoutTemplateResId,
                      CharSequence characters, int columns, int horizontalPadding) {
        super(context, layoutTemplateResId, characters, columns, horizontalPadding);
    }

    @Override
    protected Key createKeyFromXml(Resources res, Row parent, int x, int y, XmlResourceParser parser) {
        Key key = new LatinKey(res, parent, x, y, parser);
        if (key.codes[0] == 10) {
//            mEnterKey = key;
        } else if (key.codes[0] == ' ') {
//            mSpaceKey = key;
        } else if (key.codes[0] == Keyboard.KEYCODE_MODE_CHANGE) {
//            mModeChangeKey = key;
//            mSavedModeChangeKey = new LatinKey(res, parent, x, y, parser);
        } else if (key.codes[0] == -101) {
//            mLanguageSwitchKey = key;
//            mSavedLanguageSwitchKey = new LatinKey(res, parent, x, y, parser);
        }
        key.iconPreview = res.getDrawable(R.drawable.key_func_qwerty_p);
        return key;
    }



    /**
     * Dynamically change the visibility of the language switch key (a.k.a. globe key).
     * @param visible True if the language switch key should be visible.
     */
    public void setLanguageSwitchKeyVisibility(boolean visible) {
        if (visible) {
            // The language switch key should be visible. Restore the size of the mode change key
            // and language switch key using the saved layout.
//            mModeChangeKey.width = mSavedModeChangeKey.width;
//            mModeChangeKey.x = mSavedModeChangeKey.x;
//            mLanguageSwitchKey.width = mSavedLanguageSwitchKey.width;
//            mLanguageSwitchKey.icon = mSavedLanguageSwitchKey.icon;
//            mLanguageSwitchKey.iconPreview = mSavedLanguageSwitchKey.iconPreview;
        } else {
            // The language switch key should be hidden. Change the width of the mode change key
            // to fill the space of the language key so that the user will not see any strange gap.
//            mModeChangeKey.width = mSavedModeChangeKey.width + mSavedLanguageSwitchKey.width;
//            mLanguageSwitchKey.width = 0;
//            mLanguageSwitchKey.icon = null;
//            mLanguageSwitchKey.iconPreview = null;
        }
    }


    static class LatinKey extends Key {

        public LatinKey(Resources res, Row parent, int x, int y,
                        XmlResourceParser parser) {
            super(res, parent, x, y, parser);
        }

        /**
         * Overriding this method so that we can reduce the target area for the key that
         * closes the keyboard.
         */
        @Override
        public boolean isInside(int x, int y) {
            return super.isInside(x, codes[0] == KEYCODE_CANCEL ? y - 10 : y);
        }


    }
}
