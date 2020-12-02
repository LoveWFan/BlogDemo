#include <jni.h>
#include "drawer/triangle_drawer.h"
#include "utils/logger.h"
#include "render/image_render.h"
// 需要加上这个宏不然编译器会编译失败
#define STB_IMAGE_IMPLEMENTATION

#include "utils/stb_image.h"
#include "filter/adjust/color_invert_filter.h"
#include "filter/adjust/contrast_image_filter.h"
#include "filter/adjust/exposure_filter.h"
#include "filter/adjust/brightness_filter.h"
#include "filter/adjust/saturation_filter.h"
#include "filter/adjust/hue_filter.h"
#include "filter/adjust/sharpen_filter.h"
#include "egl/opengl_render.h"
#include <android/bitmap.h>
#include <malloc.h>
#include <string.h>

extern "C"
JNIEXPORT jlong

JNICALL
Java_com_poney_blogdemo_demo1_DemoActivity_createTriangleDrawer(JNIEnv *env, jobject thiz) {
    TriangleDrawer *triangleDrawer = new TriangleDrawer();
    return (jint) triangleDrawer;
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_poney_blogdemo_demo1_DemoActivity_createBitmapRender(JNIEnv *env, jobject thiz,
                                                              jobject bitmap) {
    AndroidBitmapInfo info; // create a AndroidBitmapInfo
    int result;
    // 获取图片信息
    result = AndroidBitmap_getInfo(env, bitmap, &info);
    if (result != ANDROID_BITMAP_RESULT_SUCCESS) {
        LOGE("Player", "AndroidBitmap_getInfo failed, result: %d", result);
        return 0;
    }
    LOGD("Player", "bitmap width: %d, height: %d, format: %d, stride: %d", info.width, info.height,
         info.format, info.stride);
    // 获取像素信息
    unsigned char *data;

    result = AndroidBitmap_lockPixels(env, bitmap, reinterpret_cast<void **>(&data));
    if (result != ANDROID_BITMAP_RESULT_SUCCESS) {
        LOGE("Player", "AndroidBitmap_lockPixels failed, result: %d", result);
        return 0;
    }
    size_t count = info.stride * info.height;

    unsigned char *resultData = (unsigned char *) malloc(count * sizeof(unsigned char));;
    memcpy(resultData, data, count);

    // 像素信息不再使用后需要解除锁定
    result = AndroidBitmap_unlockPixels(env, bitmap);
    if (result != ANDROID_BITMAP_RESULT_SUCCESS) {
        LOGE("Player", "AndroidBitmap_unlockPixels failed, result: %d", result);
    }
    ImageRender *pImageRender = new ImageRender(info.width, info.height, resultData);
    return (jint) pImageRender;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_poney_blogdemo_demo1_DemoActivity_drawBitmap(JNIEnv *env, jobject thiz, jlong drawer) {
    ImageRender *pImageRender = reinterpret_cast<ImageRender *>(drawer);
    pImageRender->DoDraw();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_poney_blogdemo_demo1_DemoActivity_releaseNative(JNIEnv *env, jobject thiz, jlong render) {
    ImageRender *pImageRender = reinterpret_cast<ImageRender *>(render);
    pImageRender->Release();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_poney_blogdemo_demo1_DemoActivity_onOutputSizeChanged(JNIEnv *env, jobject thiz,
                                                               jlong render, jint width,
                                                               jint height) {
    ImageRender *pImageRender = reinterpret_cast<ImageRender *>(render);
    if (pImageRender != NULL)
        pImageRender->OnOutputSizeChanged(width, height);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_poney_blogdemo_demo1_DemoActivity_setFilterNative(JNIEnv *env, jobject thiz, jlong render,
                                                           jlong filter) {
    ImageRender *pImageRender = reinterpret_cast<ImageRender *>(render);
    ImageFilter *pImageFilter = reinterpret_cast<ImageFilter *>(filter);
    if (pImageRender != NULL && pImageFilter != NULL) {
        BaseDrawer *old_filter = pImageRender->getFilter();
        if (old_filter != NULL)
            old_filter->Release();
        pImageRender->setFilter(pImageFilter);
    }
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_poney_blogdemo_demo1_DemoActivity_createFilterByTypeNative(JNIEnv *env, jobject thiz,
                                                                    jint filter_type) {
    ImageFilter *pImageFilter = NULL;

    switch (filter_type) {
        case 0:
            pImageFilter = new ImageFilter();
            break;
        case 1:
            pImageFilter = new ColorInvertImageFilter();
            break;
        case 2:
            pImageFilter = new ContrastImageFilter();
            break;
        case 3:
            pImageFilter = new BrightnessFilter();
            break;
        case 4:
            pImageFilter = new ExposureFilter();
            break;
        case 5:
            pImageFilter = new HueFilter();
            break;
        case 6:
            pImageFilter = new SaturationFilter();
            break;
        case 7:
            pImageFilter = new SharpenFilter();
            break;
    }

    return reinterpret_cast<jlong>(pImageFilter);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_poney_blogdemo_demo1_DemoActivity_adjustFilterProgressNative(JNIEnv *env, jobject thiz,
                                                                      jint filter_type,
                                                                      jlong filter, jfloat value) {

    switch (filter_type) {
        case 2: {
            auto *pImageFilter = reinterpret_cast<ContrastImageFilter *>(filter);
            if (pImageFilter != NULL)
                pImageFilter->setValue(value);
        }
            break;
        case 3: {
            auto *pImageFilter = reinterpret_cast<BrightnessFilter *>(filter);
            if (pImageFilter != NULL)
                pImageFilter->setValue(value);
        }
            break;
        case 4: {
            auto *pImageFilter = reinterpret_cast<ExposureFilter *>(filter);
            if (pImageFilter != NULL)
                pImageFilter->setValue(value);
        }
            break;
        case 5: {
            auto *pImageFilter = reinterpret_cast<HueFilter *>(filter);
            if (pImageFilter != NULL)
                pImageFilter->setValue(value);
        }
            break;
        case 6: {
            auto *pImageFilter = reinterpret_cast<SaturationFilter *>(filter);
            if (pImageFilter != NULL)
                pImageFilter->setValue(value);
        }
            break;
        case 7: {
            auto *pImageFilter = reinterpret_cast<SharpenFilter *>(filter);
            if (pImageFilter != NULL)
                pImageFilter->setValue(value);
        }
            break;
    }


}extern "C"
JNIEXPORT jint JNICALL
Java_com_poney_blogdemo_demo1_EGLDemoActivity_createGLRender(JNIEnv *env, jobject thiz,
                                                             jobject surface) {
    OpenGLRender *glRender = new OpenGLRender(env);

    glRender->SetSurface(surface);
    return (jint) glRender;

}

extern "C"
JNIEXPORT void JNICALL
Java_com_poney_blogdemo_demo1_EGLDemoActivity_showBitmap(JNIEnv *env, jobject thiz, jint render,
                                                         jobject bitmap) {
    AndroidBitmapInfo info; // create a AndroidBitmapInfo
    int result;
    // 获取图片信息
    result = AndroidBitmap_getInfo(env, bitmap, &info);
    if (result != ANDROID_BITMAP_RESULT_SUCCESS) {
        LOGE("Player", "AndroidBitmap_getInfo failed, result: %d", result);
        return;
    }
    LOGD("Player", "bitmap width: %d, height: %d, format: %d, stride: %d", info.width, info.height,
         info.format, info.stride);
    // 获取像素信息
    unsigned char *data;

    result = AndroidBitmap_lockPixels(env, bitmap, reinterpret_cast<void **>(&data));
    if (result != ANDROID_BITMAP_RESULT_SUCCESS) {
        LOGE("Player", "AndroidBitmap_lockPixels failed, result: %d", result);
        return;
    }
    size_t count = info.stride * info.height;

    unsigned char *resultData = (unsigned char *) malloc(count * sizeof(unsigned char));;
    memcpy(resultData, data, count);

    // 像素信息不再使用后需要解除锁定
    result = AndroidBitmap_unlockPixels(env, bitmap);
    if (result != ANDROID_BITMAP_RESULT_SUCCESS) {
        LOGE("Player", "AndroidBitmap_unlockPixels failed, result: %d", result);
    }
    ImageRender *pImageRender = new ImageRender(info.width, info.height, resultData);
    OpenGLRender *pGLRender = reinterpret_cast<OpenGLRender *>(render);
    pGLRender->SetBitmapRender(pImageRender);

}

extern "C"
JNIEXPORT void JNICALL
Java_com_poney_blogdemo_demo1_EGLDemoActivity_releaseGLRender(JNIEnv *env, jobject thiz,
                                                              jint render) {
    OpenGLRender *pGLRender = reinterpret_cast<OpenGLRender *>(render);
    pGLRender->ReleaseRender();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_poney_blogdemo_demo1_EGLDemoActivity_switchToFilterNative(JNIEnv *env, jobject thiz,
                                                                   jint render, jint filter_type) {


}

extern "C"
JNIEXPORT void JNICALL
Java_com_poney_blogdemo_demo1_EGLDemoActivity_adjust(JNIEnv *env, jobject thiz, jint render,
                                                     jint progress, jint filter_type) {

}