package com.example.vstar.cameraplaydemo.decoder;

import android.content.res.AssetFileDescriptor;
import android.view.Surface;

import java.io.File;

public class DecoderThread extends DecoderCore implements Runnable {
    private static final String TAG = "DecoderThread";
    
    public DecoderThread(File videoFile, Surface outputSurface, SpeedControlCallback cb) {
    	super(videoFile, outputSurface, cb);	
    }
    
    public DecoderThread(AssetFileDescriptor afd, Surface outputSurface, SpeedControlCallback cb) {
    	super(afd, outputSurface, cb);	
    }
	
    public DecoderThread(File videoFile, Surface outputSurface) {
    	super(videoFile, outputSurface);	
    }

    @Override
    public void run() {
    	long start = System.currentTimeMillis();
    	doDecode(); //core components for decoding
    }
	
    public void startPlaying() {
    	new Thread(this, "MyDecoder").start();
    }
	
    public void stopPlaying() {
    	super.stopThread = true;
    }
}
