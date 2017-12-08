//
// Created by lenovo on 2017/12/8.
//
#include <jni.h>

extern "C" {
jstring
Java_com_example_vstar_cameraplaydemo_ndk_ImageUtilEngine_javaCallCC(
        JNIEnv *env,
        jobject /* this */) {
    return env->NewStringUTF("call static native mathod C++");
}
}
