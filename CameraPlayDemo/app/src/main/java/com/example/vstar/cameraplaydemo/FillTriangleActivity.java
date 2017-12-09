package com.example.vstar.cameraplaydemo;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;

import com.example.vstar.cameraplaydemo.SurfaceView.CameraView;
import com.example.vstar.cameraplaydemo.SurfaceView.GL2JNISurfaceView;
import com.example.vstar.cameraplaydemo.SurfaceView.SporeRender;

public class FillTriangleActivity extends Activity {
    GL2JNISurfaceView mView;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = new GL2JNISurfaceView(getApplication());
        setContentView(mView);
    }

    @Override protected void onPause() {
        super.onPause();
        mView.onPause();
    }

    @Override protected void onResume() {
        super.onResume();
        mView.onResume();
    }
}