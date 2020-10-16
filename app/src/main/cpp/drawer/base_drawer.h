//
// Created by feibiao.ma on 2020/10/12.
//

#ifndef BLOGDEMO_BASE_DRAWER_H
#define BLOGDEMO_BASE_DRAWER_H

#include <GLES2/gl2.h>

class BaseDrawer {
protected:
    const char *TAG = "BaseDrawer";
    GLuint m_program_id = 0;

    //顶点坐标接收者
    GLint m_vertex_pos_handler = -1;

    GLuint m_texture_id = 0;
    //纹理坐标接收者
    GLint m_texture_pos_handler = -1;
    //纹理接收者
    GLint m_texture_handler = -1;

    void CreateProgram();

    GLuint LoadShader(GLenum type, const GLchar *shader_code);


public:

    BaseDrawer();

    ~BaseDrawer();


    virtual void Release() = 0;

    virtual void DoDraw() = 0;

    virtual const GLchar *GetVertexShader() = 0;

    virtual const GLchar *GetFragmentShader() = 0;

    virtual void InitVarHandler() = 0;
};


#endif //BLOGDEMO_BASE_DRAWER_H
