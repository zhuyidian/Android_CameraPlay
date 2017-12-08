package com.example.vstar.cameraplaydemo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.vstar.cameraplaydemo.SurfaceView.SVDraw;

import java.io.IOException;

public class CameraSurfaceAndDrawSurfaceActivity extends AppCompatActivity {
    private SVDraw surfaceDraw = null;
    private SurfaceView surfaceView = null;
    private SurfaceHolder holder1 = null;
    private Canvas canvas = null;
    private Camera cam = null;
    private boolean previewRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_draw);

        //绘制surface1
        surfaceView = (SurfaceView) findViewById(R.id.surface1);
        holder1 = surfaceView.getHolder();
        holder1.addCallback(new MySurfaceViewCallback());
        holder1.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //holder1.setFixedSize(500, 350);
        holder1.setFormat(PixelFormat.TRANSPARENT);

        canvas = holder1.lockCanvas();
        if (canvas != null) {
            canvas.drawColor(Color.TRANSPARENT);
            Paint p = new Paint();
            p.setAntiAlias(true);
            p.setColor(Color.RED);
            p.setStyle(Paint.Style.STROKE);
            // canvas.drawPoint(100.0f, 100.0f, p);
            canvas.drawLine(0, 110, 500, 110, p);
        }

        // 绘制surface2  其中使用线程
        surfaceDraw = (SVDraw) findViewById(R.id.mDraw);
        surfaceDraw.setVisibility(View.VISIBLE);
        surfaceDraw.drawLine();
    }

    // =============================create surface 1=================================================
    private class MySurfaceViewCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            cam = Camera.open(); // 取得第一个摄像头
            Camera.Parameters param = cam.getParameters();
            // param.setPreviewSize(display.getWidth(), display.getHeight()) ;
            param.setPreviewFrameRate(5); // 一秒5帧
            param.setPictureFormat(PixelFormat.JPEG); // 图片形式
            param.set("jpen-quality", 80);
            cam.setParameters(param);
            cam.setDisplayOrientation(90); // 纠正摄像头自动旋转，纠正角度，如果引用，则摄像角度偏差90度

            try {
                cam.setPreviewDisplay(holder);
            } catch (IOException e) {
            }

            cam.startPreview(); // 进行预览
            previewRunning = true; // 已经开始预览
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (cam != null) {
                if (previewRunning) {
                    cam.stopPreview(); // 停止预览
                    previewRunning = false;
                }
                cam.release();
            }
        }
    }

}
