package com.example.vstar.cameraplaydemo.SurfaceView;

import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

import com.example.vstar.cameraplaydemo.util.Square;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLRendererDraw implements Renderer {

	private Square square;

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// 黑色背景
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		// 启用阴影平滑（不是必须的）
		gl.glShadeModel(GL10.GL_SMOOTH);
		// 设置深度缓存
		gl.glClearDepthf(1.0f);
		// 启用深度测试
		gl.glEnable(GL10.GL_DEPTH_TEST);
		// 所作深度测试的类型
		gl.glDepthFunc(GL10.GL_LEQUAL);
		// 对透视进行修正
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

		square = new Square();
	}

	public void onDrawFrame(GL10 gl) {
		// 清除屏幕和深度缓存
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		/**
		 * 如果你现在运行应用，我们又看到了华丽的黑屏，为什么？
		 * 因为OpenGL ES渲染默认的当前位置为(0,0,0)，窗口的定位也一样
		 * 而且OpenGL ES不渲染太靠近窗体定位的东西。解决方法就是移动绘制的位置。
		 * 在屏幕中移动四个单位
		 */
		gl.glTranslatef(0, 0, -4);
		// 绘制正方形
		square.draw(gl);
		/**
		 * 运行应用你将看到该正方形已经被绘制，但是它好像离我们越来越远一样，最后消失了
		 * OpenGL ES不会在画面之间复位绘制点，所以我们要自己完成
		 * 重置当前的模型观察矩阵
		 */
		gl.glLoadIdentity();
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// 设置画面的大小
		gl.glViewport(0, 0, width, height);
		// 设置投影矩阵
		gl.glMatrixMode(GL10.GL_PROJECTION);
		// 重置投影矩阵
		gl.glLoadIdentity();
		// 设置画面比例
		GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
		// 选择模型观察矩阵
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		// 重置模型观察矩阵
		gl.glLoadIdentity();
	}
}