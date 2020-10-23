//
// Created by feibiao.ma on 2020/10/16.
//

#ifndef BLOGDEMO_BITMAP_DRAWER_H
#define BLOGDEMO_BITMAP_DRAWER_H


#include "base_drawer.h"

class BitmapDrawer : public BaseDrawer {
private:
    const char *TAG = "BitmapDrawer";
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


//
    GLfloat m_texture_coors_wrap[8] = {
            0.0f, 1.0f, // 左下角
            2.0f, 1.0f, // 右下角
            0.0f, -1.0f,//左上
            2.0f, -1.0f,//右上
    };
    /*
    const GLfloat m_texture_coors[6] = {
            0.0f, 1.0f,//左下
            1.0f, 1.0f,//右下
            0.0f, 0.0f//上
    };
    */
    // 自定义用户数据，可用于存放画面数据
    void *cst_data = NULL;


    void ActivateTexture();

public:
    BitmapDrawer(int origin_width, int origin_height, void *p);

    ~BitmapDrawer();


    void Release() override;

    void DoDraw() override;

    void InitVarHandler() override;

    const GLchar *GetVertexShader() override;

    const GLchar *GetFragmentShader() override;

    void AdjustImageScale();

    void OnOutputSizeChanged(int outputWidth, int outputHeight);

    float addDistance(const GLfloat d, float horizontal);
};


#endif //BLOGDEMO_BITMAP_DRAWER_H
