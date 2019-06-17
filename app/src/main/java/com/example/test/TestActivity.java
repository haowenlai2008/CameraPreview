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
        initButton();
    }

    //视图初始化
    private void initView() {
        surfaceView = findViewById(R.id.surfaceView);//获得SurfaceView的实例
        mSurfaceHolder = surfaceView.getHolder();//获得SurfaceView的Holder
        mSurfaceHolder.addCallback(this);//设置Holder的回调
    }

    //打开照相机
    public void CameraOpen() {
        try
        {
            //打开摄像机
            mCamera = Camera.open(cameraId);
            mCamera.setDisplayOrientation(90);
            //绑定Surface并开启预览
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            mCamera.release();
            mCamera = null;
            Toast.makeText(TestActivity.this, "surface created failed", Toast.LENGTH_SHORT).show();
        }
    }

    //回调初始化
    private void initButton() {
        //返回上个界面的按钮
        mButton = findViewById(R.id.button2);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(TestActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        //翻转摄像机的按钮
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

    //翻转摄像机
    public void CameraSwitch()
    {
        cameraId = cameraId == 1 ? 0 : 1;
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        CameraOpen();
    }

}
