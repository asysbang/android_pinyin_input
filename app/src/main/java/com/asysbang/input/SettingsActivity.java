package com.asysbang.input;

import android.app.Activity;
import android.os.Bundle;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void requestPermissions() {
        requestPermissions(new String[]{""},9);
    }
}
