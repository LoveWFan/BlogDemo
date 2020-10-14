//
// Created by feibiao.ma on 2020/10/12.
//

#ifndef BLOGDEMO_TRIANGLE_DRAWER_H
#define BLOGDEMO_TRIANGLE_DRAWER_H


#include "base_drawer.h"
#include <GLES2/gl2.h>

class TriangleDrawer : public BaseDrawer {
private:
    const char *TAG = "Drawer";

    const GLfloat m_vertex_coors[9] = {
            -0.5f, -0.5f, 0.0f,//左下
            0.5f, -0.5f, 0.0f,//右下
            0.0f, 0.5f, 0.0f//上
    };

    GLuint m_program_id = 0;

    GLint m_vertex_pos_handler = -1;

    void CreateProgram();

    GLuint LoadShader(GLenum type, const GLchar *shader_code);

public:
    TriangleDrawer();

    ~TriangleDrawer();


    void Draw() override;

    void Release() override;

    void DoDraw();

    const GLchar *GetVertexShader();

    const GLchar *GetFragmentShader();
};


#endif //BLOGDEMO_TRIANGLE_DRAWER_H
