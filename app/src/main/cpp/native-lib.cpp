#include <jni.h>
#include "drawer/triangle_drawer.h"
#include "utils/logger.h"
#include "drawer/bitmap_drawer.h"
// 需要加上这个宏不然编译器会编译失败
#define STB_IMAGE_IMPLEMENTATION

#include "utils/stb_image.h"
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
JNIEXPORT void JNICALL
Java_com_poney_blogdemo_demo1_DemoActivity_drawTriangle(JNIEnv *env, jobject thiz, jlong drawer) {
    TriangleDrawer *triangleDrawer = reinterpret_cast<TriangleDrawer *>(drawer);
    triangleDrawer->DoDraw();
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_poney_blogdemo_demo1_DemoActivity_createBitmapDrawer(JNIEnv *env, jobject thiz,
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

    LOGE("Player", "count: %d", count);
    unsigned char *resultData = (unsigned char *) malloc(count * sizeof(unsigned char));;
    memcpy(resultData, data, count);

    // 像素信息不再使用后需要解除锁定
    result = AndroidBitmap_unlockPixels(env, bitmap);
    if (result != ANDROID_BITMAP_RESULT_SUCCESS) {
        LOGE("Player", "AndroidBitmap_unlockPixels failed, result: %d", result);
    }
    BitmapDrawer *bitmapDrawer = new BitmapDrawer(info.width, info.height, resultData);
    return (jint) bitmapDrawer;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_poney_blogdemo_demo1_DemoActivity_drawBitmap(JNIEnv *env, jobject thiz, jlong drawer) {
    BitmapDrawer *pBitmapDrawer = reinterpret_cast<BitmapDrawer *>(drawer);
    pBitmapDrawer->DoDraw();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_poney_blogdemo_demo1_DemoActivity_release(JNIEnv *env, jobject thiz, jlong drawer) {
    BaseDrawer *pBaseDrawer = reinterpret_cast<BaseDrawer *>(drawer);
    pBaseDrawer->Release();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_poney_blogdemo_demo1_DemoActivity_onOutputSizeChanged(JNIEnv *env, jobject thiz,
                                                               jlong drawer, jint width,
                                                               jint height) {
    BitmapDrawer *pBitmapDrawer = reinterpret_cast<BitmapDrawer *>(drawer);
    if (pBitmapDrawer != NULL)
        pBitmapDrawer->OnOutputSizeChanged(width, height);
}