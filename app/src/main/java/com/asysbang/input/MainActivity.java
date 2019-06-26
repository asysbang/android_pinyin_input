package com.asysbang.input;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.asysbang.input.service.InputCoreService;
import com.asysbang.input.tess.TessHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private Button mShowInputPicker;
    private Button mRequestPermission;
    private Button mTestTess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mShowInputPicker = findViewById(R.id.show_input_picker);
        mShowInputPicker.setOnClickListener(this);

        mRequestPermission = findViewById(R.id.request_permission);
        mRequestPermission.setOnClickListener(this);

        mTestTess = findViewById(R.id.test_tess);
        mTestTess.setOnClickListener(this);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.request_permission:
                requestPermission();
                break;
            case R.id.show_input_picker:
                InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                mInputMethodManager.showInputMethodPicker();
                break;
            case R.id.test_tess:
                testTess();
                break;
        }
    }

    private void testTess() {
        TessHelper tessHelper = TessHelper.getInstance();
        tessHelper.test();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void requestPermission() {
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (PackageManager.PERMISSION_GRANTED != permission) {
            Log.e("","========PERMISSION_GRANTED");
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        } else {
            Log.e("","========PERMISSION_GRANTED have");
        }

    }
}
