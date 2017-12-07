package com.example.vstar.cameraplaydemo;

import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;

import com.example.vstar.cameraplaydemo.SurfaceView.DecoderGLSurfaceView;
import com.example.vstar.cameraplaydemo.decoder.DecoderThread;
import com.example.vstar.cameraplaydemo.decoder.SpeedController;

import java.io.File;

public class DecoderGLSurfaceActivity extends AppCompatActivity implements SurfaceTexture.OnFrameAvailableListener {
    private static final String TAG = "DecoderGLSurfaceActivity";
    private DecoderGLSurfaceView glSurfaceView;
    private SurfaceTexture surfaceTexture;//TODO: add a handler to pass the surfaceTexture from renderer.
    DecoderThread decoder; //done on a decoder thread to avoid block in UI
    boolean playingFlag;
    AssetFileDescriptor afd;
    Surface surface;
    SpeedController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoder);

        glSurfaceView = (DecoderGLSurfaceView) findViewById(R.id.glsurfaceview);

        playingFlag = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        decoder.stopPlaying();
        glSurfaceView.stopRendering();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (decoder != null) {
            decoder.stopPlaying();
            decoder = null;
        }
        if (glSurfaceView.renderer != null) {
            glSurfaceView.stopRendering();
        }
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        glSurfaceView.requestRender();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startPlayingVideo();
        }
        return super.onTouchEvent(event);
    }

    public void startPlayingVideo() {
        String dir = Environment.getExternalStorageDirectory()+"/DCIM/Eye4";
        File file = new File(dir, "VSTA000004YWFVH_20171207174446.mp4");
        surfaceTexture = glSurfaceView.getSurfaceTexture();//get surfacetexture created in renderer
        //Register a callback when a new frame is available to the SurfaceTexture
        surfaceTexture.setOnFrameAvailableListener(this);
        Surface surface = new Surface(surfaceTexture);//get the surface to be used for decoding output
        decoder = new DecoderThread(file, surface);//initialize all the decoding stuff here.
        decoder.startPlaying();//start the video decoding thread here
        playingFlag = true;//enable renderer to draw frames
    }

    public void stopVideo() {
        if (decoder != null) {
            decoder.stopPlaying();//delete the thread
            decoder = null;
            playingFlag = false;
        }
    }

    //check if it is ready to play video
    public boolean isPlaying() {
        return playingFlag;
    }


    public void click_to_play(View argu) {
        startVideo();
    }
    public void click_to_stop(View argu) {
        stopVideo();//stop the video
    }
    public void startVideo() {
        if (decoder != null) stopVideo();

        prepareVideo();
        decoder.startPlaying();//start the video decoding thread here

        playingFlag = true;

        decoder.stopPlaying();
        glSurfaceView.stopRendering();
    }
    //prepare before playing every video.
    public void prepareVideo() {
        //read video from res/raw/
        afd = getResources().openRawResourceFd(R.raw.test);
        surfaceTexture = glSurfaceView.getSurfaceTexture();//get surfacetexture created in renderer
        //Register a callback when a new frame is available to the SurfaceTexture
        surfaceTexture.setOnFrameAvailableListener(this);
        surface = new Surface(surfaceTexture);//get the surface to be used for decoding output
        controller = new SpeedController();
        decoder = new DecoderThread(afd, surface, controller);//initialize all the decoding stuff here.
    }
}
