//
// Created by poney.ma on 2020/9/16.
//

#include "egl_surface.h"
#include "../utils/logger.h"

EglSurface::EglSurface() {
    m_core = new EglCore();
}

EglSurface::~EglSurface() {
    delete m_core;
}

bool EglSurface::Init() {
    return m_core->Init(NULL);
}
/**
 *
 * @param native_window 传入上一步创建的ANativeWindow
 * @param width
 * @param height
 */
void EglSurface::CreateEglSurface(ANativeWindow *native_window, int width, int height) {
    if (native_window != NULL) {
        this->m_native_window = native_window;
        m_surface = m_core->CreateWindSurface(m_native_window);
    } else {
        m_surface = m_core->CreateOffScreenSurface(width, height);
    }
    if (m_surface == NULL) {
        LOGE(TAG, "EGL create window surface fail")
        Release();
    }
    MakeCurrent();
}

void EglSurface::MakeCurrent() {
    m_core->MakeCurrent(m_surface);
}

void EglSurface::SwapBuffers() {
    m_core->SwapBuffer(m_surface);
}

void EglSurface::DestroyEglSurface() {
    if (m_surface != NULL) {
        if (m_core != NULL) {
            m_core->DestroySurface(m_surface);
        }
        m_surface = NULL;
    }
}

void EglSurface::Release() {
    DestroyEglSurface();
    if (m_core != NULL) {
        m_core->Release();
    }
}
