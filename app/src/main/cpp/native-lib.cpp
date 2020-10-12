#include <jni.h>
#include "drawer/triangle_drawer.h"

extern "C"
JNIEXPORT jint

JNICALL
Java_com_poney_blogdemo_demo1_DemoActivity_createTriangleDrawer(JNIEnv *env, jobject thiz) {
    TriangleDrawer *triangleDrawer = new TriangleDrawer();
    return (jint) triangleDrawer;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_poney_blogdemo_demo1_DemoActivity_drawTriangle(JNIEnv *env, jobject thiz, jint drawer) {
    TriangleDrawer *triangleDrawer = reinterpret_cast<TriangleDrawer *>(drawer);
    triangleDrawer->Draw();

}