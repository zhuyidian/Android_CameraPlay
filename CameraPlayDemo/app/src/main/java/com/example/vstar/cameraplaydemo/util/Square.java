package com.example.vstar.cameraplaydemo.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.opengles.GL10;

public class Square {
	// 顶点坐标数组
	private float vertices[] = { -1.0f, 1.0f, 0.0f, // 0, 左上
			-1.0f, -1.0f, 0.0f, // 1, 左下
			1.0f, -1.0f, 0.0f, // 2, 右下
			1.0f, 1.0f, 0.0f, // 3, 右上
	};
	// 连接规则
	private short[] indices = { 0, 1, 2, 0, 2, 3 };
	// 顶点缓存
	private FloatBuffer vertexBuffer;
	// 索引缓存
	private ShortBuffer indexBuffer;

	public Square() {
		// 一个float为4 bytes, 因此要乘以4
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		// short类型同理
		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indexBuffer = ibb.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);
	}

	/**
	 * 绘制正方形到屏幕
	 *
	 * @param gl
	 */
	public void draw(GL10 gl) {
		// 逆时针环绕
		gl.glFrontFace(GL10.GL_CCW);
		// 开启剔除功能
		gl.glEnable(GL10.GL_CULL_FACE);
		// 剔除背面
		gl.glCullFace(GL10.GL_BACK);
		// 开启顶点缓存写入功能
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		// 设置顶点
		// size:每个顶点有几个数指描述。
		// type:数组中每个顶点的坐标类型。
		// stride:数组中每个顶点间的间隔，步长（字节位移）。
		// pointer:存储着每个顶点的坐标值。初始值为0
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_SHORT, indexBuffer);
		// 关闭各个功能
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisable(GL10.GL_CULL_FACE);
	}
}
