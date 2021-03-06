# Copyright (C) 2009 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#


#LOCAL_PATH := $(call my-dir)
#include $(CLEAR_VARS)
#LOCAL_SRC_FILES:= \com_spore_jni_ImageUtilEngine.c
#LOCAL_C_INCLUDES := \ $(JNI_H_INCLUDE)
#LOCAL_SHARED_LIBRARIES := libutils
#LOCAL_PRELINK_MODULE := false
#LOCAL_MODULE := libJNITest
#LOCAL_LDLIBS    :=  -llog -ljnigraphics
#include $(BUILD_SHARED_LIBRARY)


LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE    := EncodeJni

LOCAL_SRC_FILES := com_spore_jni_ImageUtilEngine.c \
                   decoderNDK.c \
                   EncodeNDK.cpp \
                   render.c \
                   opengles_code.cpp
LOCAL_C_INCLUDES := \ $(JNI_H_INCLUDE)

LOCAL_LDLIBS    :=  -llog -ljnigraphics
LOCAL_LDLIBS    +=  -lGLESv2

# use GL ext model
#LOCAL_CFLAGS += -DGL_GLEXT_PROTOTYPES

include $(BUILD_SHARED_LIBRARY)
