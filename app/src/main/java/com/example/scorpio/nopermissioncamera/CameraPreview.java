package com.example.scorpio.nopermissioncamera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by Scorpio on 16/4/8.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private final Camera mCamera;
    private final SurfaceHolder mHolder;
    private static final String TAG = "CameraPreview";

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        //Install a SurfaceHolder.Callback so we get notified when the underlying surface is created and destroyed
        mHolder = getHolder();
        mHolder.addCallback(this);
        //deprecated setting,but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //the Surface has been created,now tell the camera where to draw the perbiew
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(TAG, "Error setting camera preview" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        //empty. Take care of releasing the Camera preview in your activity
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //if your preview can change or rotate, take care of those events here.
        //Make sure to stop the preview before resizing or reformatting it

        if (mHolder.getSurface() == null) {
            //preview surface does not exist
            return;
        }
        //stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            //ignore:tried to stop a non-existent preview
            e.printStackTrace();
        }
        
        //set preview size and make any resize,rotate or reformatting changes here
        
        //start preview with new settings
        try {
            
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error starting camera preview:" + e.getMessage());
            e.printStackTrace();
        }
    }
}
