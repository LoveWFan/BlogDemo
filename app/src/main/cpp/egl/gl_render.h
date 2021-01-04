//
// Created by poney.ma on 2020/9/16.
//

#ifndef WHEAT_OPENGL_RENDER_H
#define WHEAT_OPENGL_RENDER_H


#include <jni.h>
#include <android/native_window.h>
#include <android/native_window_jni.h>
#include <memory>
#include <thread>
#include "egl_surface.h"
#include "../render/image_render.h"

class GLRender {
private:

    const char *TAG = "GLRender";

    //OpenGL渲染状态
    enum STATE {
        NO_SURFACE, //没有有效的surface
        FRESH_SURFACE, //持有一个为初始化的新的surface
        RENDERING, //初始化完毕，可以开始渲染
        SURFACE_DESTROY, //surface销毁
        STOP //停止绘制
    };

    JNIEnv *m_env = NULL;

    //线程依附的jvm环境
    JavaVM *m_jvm_for_thread = NULL;

    //Surface引用，必须要使用引用，否则无法在线程中操作
    jobject m_surface_ref = NULL;

    //本地屏幕
    ANativeWindow *m_native_window = NULL;

    //EGL显示表面
    EglSurface *m_egl_surface = NULL;

    int m_window_width = 0;
    int m_window_height = 0;
    // 绘制代理器
    ImageRender *pImageRender = NULL;

    STATE m_state = NO_SURFACE;

    // 初始化相关的方法
    void InitRenderThread();

    bool InitEGL();

    void InitDspWindow(JNIEnv *env);

    // 创建/销毁 Surface
    void CreateSurface();


    void DestroySurface();

    // 渲染方法
    void Render();

    void ReleaseSurface();

    void ReleaseWindow();

    // 渲染线程回调方法
    static void sRenderThread(std::shared_ptr<GLRender> that);

public:
    GLRender(JNIEnv *env);

    ~GLRender();

    void SetSurface(jobject surface);

    void SetOffScreenSize(int width, int height);

    void Stop();

    void SetBitmapRender(ImageRender *bitmapRender);


// 释放资源相关方法
    void ReleaseRender();

    ImageRender *GetImageRender();
};


#endif //WHEAT_OPENGL_RENDER_H
