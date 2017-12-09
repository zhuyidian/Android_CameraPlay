/**
 * Name        : ImageUtilEngine.java
 * Copyright   : Copyright (c) Tencent Inc. All rights reserved.
 * Description : TODO
 */

package com.example.vstar.cameraplaydemo.ndk;

import android.graphics.Bitmap;

/**
 * @author ianmao
 */
public class ImageUtilEngine {

    static {
        System.loadLibrary("EncodeJni");
    }

    //图片处理
    public static native int[] decodeYUV420SP(byte[] buf, int width, int heigth);
    // native方法 调用 C代码  测试
    public static native String javaCallC();
    public static native String javaCallCC();
    //GLSurface JNI渲染
    public static native void init();
    public static native void resize(int width, int height);
    public static native void render();
    public static native void initGL2(int width, int height);
    public static native void stepGL2();
}
