//
// Created by feibiao.ma on 2020/10/16.
//

#ifndef BLOGDEMO_BITMAP_DRAWER_H
#define BLOGDEMO_BITMAP_DRAWER_H


#include "base_drawer.h"

class BitmapDrawer : public BaseDrawer {
private:
    const char *TAG = "BitmapDrawer";
    int m_origin_width = 0;

    int m_origin_height = 0;

    //顶点坐标
    const GLfloat m_vertex_coors[8] = {
            -1.0f, -1.0f,//左下
            1.0f, -1.0f,//右下
            -1.0f, 1.0f,//左上
            1.0f, 1.0f//右上
    };

    //纹理坐标
    const GLfloat m_texture_coors[8] = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f
    };

    // 自定义用户数据，可用于存放画面数据
    void *cst_data = NULL;


    void ActivateTexture(GLenum type = GL_TEXTURE_2D, GLuint texture = -1,
                         GLenum index = 0, int texture_handler = -1);

public:
    BitmapDrawer(int origin_width, int origin_height, void *p);

    ~BitmapDrawer();


    void Release() override;

    void DoDraw() override;

    void InitVarHandler() override;

    const GLchar *GetVertexShader() override;

    const GLchar *GetFragmentShader() override;
};


#endif //BLOGDEMO_BITMAP_DRAWER_H
