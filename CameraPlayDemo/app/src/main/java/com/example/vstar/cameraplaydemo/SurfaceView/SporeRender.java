/**
 * Name        : SporeRender.java
 * Copyright   : Copyright (c) Tencent Inc. All rights reserved.
 * Description : TODO
 */
package com.example.vstar.cameraplaydemo.SurfaceView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;

import com.example.vstar.cameraplaydemo.R;
import com.example.vstar.cameraplaydemo.util.Texture2D;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author ianmao
 * 
 */
public class SporeRender implements GLSurfaceView.Renderer
{
    Context mContext;
    Bitmap mBitmap;
    Texture2D mTexture2d;
    private static Object INSTANCE_LOCK = new Object();
    //byte[] mFrameBuf = new byte[480*640*3];
    public int[] mFrameBuf = new int[480*640];
    boolean useBuf = false;
    int mWidth,mHeight;

    public SporeRender(Context context)
    {
        mContext = context;
        mBitmap =  BitmapFactory.decodeResource(mContext.getResources(), R.drawable.img_3);
        mTexture2d = new Texture2D();
    }
    
    public void update(int[] data,int width,int height)
    {
        mBitmap = Bitmap.createBitmap(data, width, height, Config.RGB_565);
        useBuf = false;
        mWidth = width;
      mHeight = height;
//        System.arraycopy(data, 0, mFrameBuf, 0, data.length);
//        mWidth = width;
//        mHeight = height;
//        useBuf = false;
    }
    
    public void update(Bitmap bmp)
    {
        mBitmap = bmp;
        useBuf = false;
    }
    
    public void update(byte[] data,int width,int height)
    {
        System.arraycopy(data, 0, mFrameBuf, 0, data.length);
        mWidth = width;
        mHeight = height;
        useBuf = true;
    }
  
    public void onDrawFrame(GL10 gl)
    {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glTranslatef(0f, 0f, 0f);

//        Texture2D texture2d = new Texture2D(mBitmap);
//        texture2d.draw(gl, 0, 0);
//        texture2d.delete(gl);
        if(useBuf)
            mTexture2d.bind(gl, mFrameBuf,mWidth,mHeight);
        else
            mTexture2d.bind(gl, mBitmap);
        mTexture2d.draw(gl, 0, 0);
        mTexture2d.delete(gl);
    }

    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        //gl.glDisable(GL10.GL_DITHER);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        //gl.glClearColor(1, 1, 1, 1);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glShadeModel(GL10.GL_SMOOTH);
    }
}
