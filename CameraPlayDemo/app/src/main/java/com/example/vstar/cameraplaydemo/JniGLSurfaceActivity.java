package com.example.vstar.cameraplaydemo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vstar.cameraplaydemo.ndk.ImageUtilEngine;

import java.io.File;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 视频播放类
 * @author zhognwr
 */
public class JniGLSurfaceActivity extends AppCompatActivity implements View.OnClickListener {
    private GLSurfaceView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new GLSurfaceView(JniGLSurfaceActivity.this);
        view.setEGLContextClientVersion(2);
        //set the renderer
        view.setRenderer(new CMRenderer());
        setContentView(view);
    }
    @Override
    protected void onPause() {
        super.onPause();
        view.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.onResume();
    }

    @Override
    public void onClick(View v) {

    }

    class CMRenderer implements GLSurfaceView.Renderer {

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
//			gl.glClearColor(0.5f, 0.5f, 1.0f, 1.0f);
            ImageUtilEngine.init();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
//			gl.glViewport(0, 0, width, height);
            ImageUtilEngine.resize(width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
//			gl.glClear(gl.GL_COLOR_BUFFER_BIT);
            ImageUtilEngine.render();
        }

    }
}
