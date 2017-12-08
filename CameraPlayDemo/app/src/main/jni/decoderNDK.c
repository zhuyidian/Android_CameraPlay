//
// Created by lenovo on 2017/12/8.
//
#include <jni.h>

jstring
Java_com_example_vstar_cameraplaydemo_ndk_ImageUtilEngine_javaCallC(
        JNIEnv *env,
        jobject instance/* this */) {
    return (*env)->NewStringUTF(env,"call static native mathod C");
}