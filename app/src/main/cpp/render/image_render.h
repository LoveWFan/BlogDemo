//
// Created by feibiao.ma on 2020/10/16.
//

#ifndef BLOGDEMO_IMAGE_RENDER_H
#define BLOGDEMO_IMAGE_RENDER_H


#include "../drawer/base_drawer.h"

class ImageRender {
private:
    const char *TAG = "ImageRender";
    float m_origin_width = 0;

    float m_origin_height = 0;


    float m_output_width = 0;

    float m_output_height = 0;


    //顶点坐标
    GLfloat m_vertex_coors[12] = {
            -1.0f, -1.0f, 0.0f,//左下
            1.0f, -1.0f, 0.0f,//右下
            -1.0f, 1.0f, 0.0f,//左上
            1.0f, 1.0f, 0.0f//右上
    };

    //纹理坐标
    GLfloat m_texture_coors[8] = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f
    };

    // 自定义用户数据，可用于存放画面数据
    void *cst_data = NULL;

    BaseDrawer *m_filter;

    GLuint m_texture_id = 0;

public:
    ImageRender(int origin_width, int origin_height, void *p);

    ~ImageRender();


    void AdjustImageScale();

    void OnOutputSizeChanged(int outputWidth, int outputHeight);

    float addDistance(const GLfloat d, float horizontal);

    void ResetTextureCoors();

    void DoDraw();

    void Release();

    BaseDrawer *getFilter() const;

    void setFilter(BaseDrawer *mFilter);
};


#endif //BLOGDEMO_IMAGE_RENDER_H
