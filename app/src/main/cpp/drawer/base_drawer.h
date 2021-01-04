//
// Created by poney.ma on 2020/10/12.
//

#ifndef BLOGDEMO_BASE_DRAWER_H
#define BLOGDEMO_BASE_DRAWER_H

#include <GLES2/gl2.h>
#include "../utils/opengl_utils.h"

class BaseDrawer {
protected:
    const char *TAG = "BaseDrawer";
    GLuint m_program_id = 0;

    //顶点坐标接收者
    GLint m_vertex_pos_handler = -1;

    //纹理坐标接收者
    GLint m_texture_pos_handler = -1;

    //纹理接收者
    GLint m_texture_handler = -1;


public:

    BaseDrawer();

    ~BaseDrawer();

    virtual void OnInit() = 0;

    virtual void Release() = 0;

    virtual void DoDraw(int textureId, void *vertexPos, void *texturePos) = 0;

    virtual const GLchar *GetVertexShader() = 0;

    virtual const GLchar *GetFragmentShader() = 0;

};


#endif //BLOGDEMO_BASE_DRAWER_H
