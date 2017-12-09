package com.example.vstar.cameraplaydemo;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;

import com.example.vstar.cameraplaydemo.SurfaceView.CameraView;
import com.example.vstar.cameraplaydemo.SurfaceView.SporeRender;
import com.example.vstar.cameraplaydemo.ndk.ImageUtilEngine;

import static com.example.vstar.cameraplaydemo.SurfaceView.IVCGLLib.TAG;

public class CameraEngineActivity extends Activity {
    CameraView mSelfView;
    GLSurfaceView mProcessView;
    LinearLayout mProcessView_Layout;
    static SporeRender mRender;
    //static ImageUtilEngine imageEngine;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.video_panel);
        
        //imageEngine = new ImageUtilEngine();

        //Log.e(TAG,"imageEngine---javaCallC="+imageEngine.javaCallC());
        //Log.e(TAG,"imageEngine---javaCallCC="+imageEngine.javaCallCC());
        
        mSelfView = (CameraView) findViewById(R.id.self_view);
        
        mProcessView_Layout = (LinearLayout) findViewById(R.id.process_view_layout);
        mProcessView = new GLSurfaceView(this);
        mProcessView_Layout.addView(mProcessView);
        mRender = new SporeRender(this);
        mProcessView.setRenderer(mRender);
    }
    
    public static SporeRender getRender(){
        return mRender;
    }
    
    //public static ImageUtilEngine getImageEngine(){
     //   return imageEngine;
    //}
}