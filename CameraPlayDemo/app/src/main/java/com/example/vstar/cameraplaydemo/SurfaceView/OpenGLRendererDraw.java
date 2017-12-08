package com.example.vstar.cameraplaydemo.SurfaceView;

import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

import com.example.vstar.cameraplaydemo.util.Square;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 GLSurfaceView是一个视图，继承至SurfaceView，它内嵌的surface专门负责OpenGL渲染。
 GLSurfaceView提供了下列特性：
 1> 管理一个surface，这个surface就是一块特殊的内存，能直接排版到android的视图view上。
 2> 管理一个EGL display，它能让opengl把内容渲染到上述的surface上。
 3> 用户自定义渲染器(render)。
 4> 让渲染器在独立的线程里运作，和UI线程分离。
 5> 支持按需渲染(on-demand)和连续渲染(continuous)。
 6> 一些可选工具，如调试。
 */
/**
 概念：
 Display(EGLDisplay) 是对实际显示设备的抽象。
 Surface（EGLSurface）是对用来存储图像的内存区域FrameBuffer的抽象，包括Color Buffer, Stencil Buffer ,Depth Buffer.
 Context (EGLContext) 存储OpenGL ES绘图的一些状态信息。
 */
/**
 使用GLSurfaceView
 通常会继承GLSurfaceView，并重载一些和用户输入事件有关的方法。
 如果你不需要重载事件方法，GLSurfaceView也可以直接使用，
 你可以使用set方法来为该类提供自定义的行为。例如，GLSurfaceView的渲染被委托给渲染器在独立的渲染线程里进行，
 这一点和普通视图不一样，setRenderer(Renderer)设置渲染器。
 */
/**
 初始化GLSurfaceView
 始化过程其实仅需要你使用setRenderer(Renderer)设置一个渲染器(render)。
 当然，你也可以修改GLSurfaceView一些默认配置。
 * setDebugFlags(int)
 * setEGLConfigChooser(boolean)
 * setEGLConfigChooser(EGLConfigChooser)
 * setEGLConfigChooser(int, int, int, int, int, int)
 * setGLWrapper(GLWrapper)
 */
/**
 定制android.view.Surface
 GLSurfaceView默认会创建像素格式为PixelFormat.RGB_565的surface。如果需要透明效果，调用getHolder().setFormat(PixelFormat.TRANSLUCENT)。
 透明(TRANSLUCENT)的surface的像素格式都是32位，每个色彩单元都是8位深度，像素格式是设备相关的，这意味着它可能是ARGB、RGBA或其它。
 */
/**
 选择EGL配置
 Android设备往往支持多种EGL配置，可以使用不同数目的通道(channel)，
 也可以指定每个通道具有不同数目的位(bits)深度。因此，在渲染器工作之前就应该指定EGL的配置。
 GLSurfaceView默认EGL配置的像素格式为RGB_656，16位的深度缓存(depth buffer)，默认不开启遮罩缓存(stencil buffer)。
 如果你要选择不同的EGL配置，请使用setEGLConfigChooser方法中的一种。
 */
/**
 设置渲染器
 总之，你必须调用setRenderer(GLSurfaceView.Renderer)来注册一个GLSurfaceView.Renderer渲染器。渲染器负责真正的GL渲染工作。
 */
/**
 渲染模式
 渲染器设定之后，你可以使用setRenderMode(int)指定渲染模式是按需(on demand)还是连续(continuous)。默认是连续渲染。
 */
/**
 Activity生命周期
 Activity窗口暂停(pause)或恢复(resume)时，GLSurfaceView都会收到通知，此时它的onPause方法和onResume方法应该被调用。
 这样做是为了让GLSurfaceView暂停或恢复它的渲染线程，以便它及时释放或重建OpenGL的资源。
 */
/**
 GLSurfaceView.Renderer 有三个方法：
 onSurfaceCreated() ：在开始渲染的时候被调用，无论什么时候OpenGL ES 渲染不得不重新被创建。（渲染是典型的丢失并重新创建当活动被暂停或恢复。）该方法一个创建长生命周期OpenGL资源（如材质）的好地方。
 onSurfaceChanged()：该方法在surface大小改变时被调用。这是设置你opengl视图端的好地方。如果相机是固定的，不会围着场景移动，你也可以在这里设置你的相机。
 onDrawFrame()：每帧的时候该方法都会被调用，这个用于画场景是可靠的。你完全可以通过调用glClear方法开清楚帧缓存，接着通过其他的opengl ES来调用画当前的场景。
 */
/**
 步骤：
 获取EGLDisplay对象
 初始化与EGLDisplay 之间的连接。
 获取EGLConfig对象
 创建EGLContext 实例
 创建EGLSurface实例
 连接EGLContext和EGLSurface.
 使用GL指令绘制图形
 断开并释放与EGLSurface关联的EGLContext对象
 删除EGLSurface对象
 删除EGLContext对象
 终止与EGLDisplay之间的连接。
 */
/**
 GLSurfaceView的绘制过程要点
 1，GLSurfaceview的渲染模式RenderMode
 在onAttachedToWindow后就启动了一个无线循环的子线程，
 该子线程完成了整个绘制流程，并系统默认是负责不断刷新重绘，刷新的帧率是60FPS。
 从这里也可以看出来，GLSurfaceView系统默认是16ms就重绘一次，这样的耗性能的重绘操作一定是要用在那种有持续动画的效果才有意义。
 当然，你也可以通过设置setRenderMode去设置主动刷新：setRenderMode
 注解中提到：系统默认mode==RENDERMODE_CONTINUOUSLY，这样系统会自动重绘；mode==RENDERMODE_WHEN_DIRTY时，只有surfaceCreate的时候会绘制一次，
 然后就需要通过requestRender()方法主动请求重绘。同时也提到，如果你的界面不需要频繁的刷新最好是设置成RENDERMODE_WHEN_DIRTY，
 这样可以降低CPU和GPU的活动，可以省电。
 */
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