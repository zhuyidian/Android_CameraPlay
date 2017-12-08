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

    public native int[] decodeYUV420SP(byte[] buf, int width, int heigth);
    // native方法 调用 C代码
    public native String javaCallC();
    public native String javaCallCC();
}
