package com.example.vstar.cameraplaydemo;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.view.View.OnClickListener;
import com.example.vstar.cameraplaydemo.CameraInterface.CameraInterface;
import com.example.vstar.cameraplaydemo.SurfaceView.CameraGLSurfaceView;
import com.example.vstar.cameraplaydemo.util.DisplayUtil;

public class CameraPlayActivity extends AppCompatActivity {
    CameraGLSurfaceView glSurfaceView = null;
    ImageButton shutterBtn;
    float previewRate = -1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_play);

        initUI();
        initViewParams();
    }

    private void initUI(){
        glSurfaceView = (CameraGLSurfaceView)findViewById(R.id.camera_textureview);
        shutterBtn = (ImageButton)findViewById(R.id.btn_shutter);
        shutterBtn.setOnClickListener(new BtnListeners());
    }
    private class BtnListeners implements OnClickListener{
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch(v.getId()){
                case R.id.btn_shutter:
                    CameraInterface.getInstance().doTakePicture();
                    break;
                default:break;
            }
        }
    }
    private void initViewParams(){
        ViewGroup.LayoutParams params = glSurfaceView.getLayoutParams();
        Point p = DisplayUtil.getScreenMetrics(this);
        params.width = p.x;
        params.height = p.y;
        previewRate = DisplayUtil.getScreenRate(this); //默认全屏的比例预览
        glSurfaceView.setLayoutParams(params);

        //手动设置拍照ImageButton的大小为120dip×120dip,原图片大小是64×64
        ViewGroup.LayoutParams p2 = shutterBtn.getLayoutParams();
        p2.width = DisplayUtil.dip2px(this, 80);
        p2.height = DisplayUtil.dip2px(this, 80);;
        shutterBtn.setLayoutParams(p2);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        glSurfaceView.bringToFront();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        glSurfaceView.onPause();
    }
}
