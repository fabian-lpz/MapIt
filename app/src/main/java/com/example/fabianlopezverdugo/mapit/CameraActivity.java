package com.example.fabianlopezverdugo.mapit;

import android.content.Context;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.IOException;

public class CameraActivity extends SurfaceView implements SurfaceHolder.Callback, View.OnClickListener {


    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;

    public CameraActivity(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mCamera.setDisplayOrientation(90);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try{
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException exception) {
            Log.v("Surface:", "SurfaceCreated: " + exception);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mSurfaceHolder.getSurface()==null){
            return;
        }
        try {
            mCamera.stopPreview();
        }catch(Exception exception){
            Log.v("Surface:", "SurfaceChanged: " + exception);
        }

        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        }catch(IOException exception){
            Log.v("Surface:", "SurfaceChanged: " + exception);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onClick(View v) {

    }
}
