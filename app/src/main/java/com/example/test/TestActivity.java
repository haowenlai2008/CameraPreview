package com.example.test;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import java.io.IOException;


public class TestActivity extends Activity implements  SurfaceHolder.Callback{
    public Button mButton;
    public Button switchBtn;
    private Camera mCamera;
    private SurfaceView surfaceView;
    private SurfaceHolder mSurfaceHolder;
    private int cameraId = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.test_layour);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
        initCallBack();
    }

    //视图初始化
    private void initView() {
        surfaceView = findViewById(R.id.surfaceView);
        mSurfaceHolder = surfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
    }

    //打开照相机
    public void CameraOpen() {
        try
        {
            mCamera = Camera.open(cameraId);
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
            Toast.makeText(TestActivity.this, "surface created", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            mCamera.release();
            mCamera = null;
            Toast.makeText(TestActivity.this, "surface created failed", Toast.LENGTH_SHORT).show();
        }
    }

    //回调初始化
    private void initCallBack() {
        mButton = findViewById(R.id.button2);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(TestActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        switchBtn = findViewById(R.id.button3);
        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraSwitch();
            }
        });

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //检查权限
        if (ContextCompat.checkSelfPermission(TestActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
        } else {
            CameraOpen();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.setParameters(parameters);
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    public void CameraSwitch()
    {
        cameraId = cameraId == 1 ? 0 : 1;
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        CameraOpen();
    }

}
