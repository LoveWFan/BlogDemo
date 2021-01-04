//
// Created by poney.ma on 2020/10/12.
//

#ifndef BLOGDEMO_TRIANGLE_DRAWER_H
#define BLOGDEMO_TRIANGLE_DRAWER_H


#include "base_drawer.h"
#include <GLES2/gl2.h>

class TriangleDrawer : public BaseDrawer {
private:
    const char *TAG = "TriangleDrawer";

    const GLfloat m_vertex_coors[9] = {
            -0.5f, -0.5f, 0.0f,//左下
            0.5f, -0.5f, 0.0f,//右下
            0.0f, 0.5f, 0.0f//上
    };


public:
    TriangleDrawer();

    ~TriangleDrawer();


    void Release() override;

    void DoDraw(int textureId, void *vertexPos, void *texturePos) override;

    void OnInit() override;

    const GLchar *GetVertexShader() override;

    const GLchar *GetFragmentShader() override;
};


#endif //BLOGDEMO_TRIANGLE_DRAWER_H
