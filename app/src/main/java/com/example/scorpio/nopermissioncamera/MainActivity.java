package com.example.scorpio.nopermissioncamera;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private ImageView iv;
    private CameraPreview mPreview;
    private FrameLayout preview;
    private Camera mCamera;
    private File file;
    private FileOutputStream fos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView) findViewById(R.id.iv);
        //create an instance of Camera
        mCamera = getCameraInstace();
        //create our Preview and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        
    }

    public void click(View view) {
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                mCamera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        try {
                            file = new File(Environment.getExternalStorageDirectory(), SystemClock.uptimeMillis()+".jpg");
                            fos = new FileOutputStream(file);
                            fos.write(data);
                            fos.close();
                            Toast.makeText(getApplicationContext(),"成功",Toast.LENGTH_SHORT).show();
                            mCamera.startPreview();
                            
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        });
    }
        

    /*获取一个照相机实例*/
    public static Camera getCameraInstace() {
        Camera c =null;
        try {
            c = Camera.open();//attempt to get Camera instance
        } catch (Exception e) {
            //Camera is not availavle (in use or does not exist)
            e.printStackTrace();
        }
        return c;//returns null if camera is unavailable
    }

    @Override
    protected void onDestroy() {
        mCamera.stopPreview();
        mCamera.release();
        mCamera=null;
        super.onDestroy();
    }
}
