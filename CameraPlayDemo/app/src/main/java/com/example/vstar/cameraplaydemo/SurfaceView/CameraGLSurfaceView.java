package com.example.vstar.cameraplaydemo.SurfaceView;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.util.AttributeSet;

import com.example.vstar.cameraplaydemo.CameraInterface.CameraInterface;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CameraGLSurfaceView extends GLSurfaceView implements Renderer, SurfaceTexture.OnFrameAvailableListener {
	private static final String TAG = "yanzi";
	Context mContext;
	SurfaceTexture mSurface;
	int mTextureID = -1;
	DirectDrawer mDirectDrawer;

	public CameraGLSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		/*
		设置了GLSurfaceView的版本
		如果没这个设置是啥都画不出来了，因为Android支持OpenGL ES1.1和2.0及最新的3.0，而且版本间差别很大
		*/
		setEGLContextClientVersion(2);
		setRenderer(this);
		/*
		设置它的模式为RENDERMODE_WHEN_DIRTY
		1,RENDERMODE_CONTINUOUSLY模式就会一直Render，
		2,如果设置成RENDERMODE_WHEN_DIRTY，就是当有数据时才rendered或者主动调用了GLSurfaceView的requestRender.
		3,默认是连续模式，很显然Camera适合脏模式，一秒30帧，当有数据来时再渲染。
		4,正因是RENDERMODE_WHEN_DIRTY所以就要告诉GLSurfaceView什么时候Render，也就是啥时候进到onDrawFrame()这个函数里
		 */
		setRenderMode(RENDERMODE_WHEN_DIRTY);
	}
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		/*
		绑定一个文理id
		与TextureView里对比可知，TextureView预览时因为实现了SurfaceTextureListener会自动创建SurfaceTexture。
		但在GLSurfaceView里则要手动创建同时绑定一个纹理ID
		 */
		mTextureID = createTextureID();
		mSurface = new SurfaceTexture(mTextureID);
		/*
		SurfaceTexture.OnFrameAvailableListener这个接口就干了这么一件事，当有数据上来后会进到
		public void onFrameAvailable(SurfaceTexture surfaceTexture) {
			// TODO Auto-generated method stub
			Log.i(TAG, "onFrameAvailable...");
			this.requestRender();
		}
		这里，然后执行requestRender()。
		 */
		mSurface.setOnFrameAvailableListener(this);
		/*
		本文在onSurfaceCreated()里打开Camera，在onSurfaceChanged()里开启预览，默认1.33的比例。
		原因是相比前两种预览，此处SurfaceTexture创建需要一定时间。如果想要开预览时由Activity发起，
		则要GLSurfaceView利用Handler将创建的SurfaceTexture传递给Activity
		 */
		mDirectDrawer = new DirectDrawer(mTextureID);
		CameraInterface.getInstance().doOpenCamera(null);
	}
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		GLES20.glViewport(0, 0, width, height);
		if(!CameraInterface.getInstance().isPreviewing()){
			CameraInterface.getInstance().doStartPreview(mSurface, 1.33f);
		}
	}
	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		mSurface.updateTexImage();
		float[] mtx = new float[16];
		mSurface.getTransformMatrix(mtx);
		/**
		 * 在onDrawFrame()里，如果不调用mDirectDrawer.draw(mtx);是啥都显示不出来的！！！
		 * 这是GLSurfaceView的特别之处。为啥呢？因为GLSurfaceView不是Android亲生的，
		 * 而Surfaceview和TextureView是。所以得自己按照OpenGL ES的流程画。
		 * 究竟mDirectDrawer.draw(mtx)里在哪获取的Buffer目前杂家还么看太明白，
		 * 貌似么有请求buffer，而是根据GLSurfaceView里创建的SurfaceTexture之前，
		 * 生成的有个纹理ID。这个纹理ID一方面跟SurfaceTexture是绑定在一起的，
		 * 另一方面跟DirectDrawer绑定，而SurfaceTexture作渲染载体
		 */
		mDirectDrawer.draw(mtx);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		CameraInterface.getInstance().doStopCamera();
	}

	@Override
	public void onFrameAvailable(SurfaceTexture surfaceTexture) {
		// TODO Auto-generated method stub
		this.requestRender();
	}

	private int createTextureID() {
		int[] texture = new int[1];
		GLES20.glGenTextures(1, texture, 0);
		GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
		GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
		GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
		return texture[0];
	}

	public SurfaceTexture _getSurfaceTexture(){
		return mSurface;
	}
}
