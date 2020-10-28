#include <jni.h>
#include "drawer/triangle_drawer.h"
#include "utils/logger.h"
#include "render/image_render.h"
// 需要加上这个宏不然编译器会编译失败
#define STB_IMAGE_IMPLEMENTATION

#include "utils/stb_image.h"
#include "filter/adjust/color_invert_filter.h"
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
                                                           jint filter_type) {
    ImageRender *pImageRender = reinterpret_cast<ImageRender *>(render);
    if (pImageRender != NULL) {
        if (filter_type == 1) {
            pImageRender->setFilter(new ColorInvertImageFilter());
        }
    }
}