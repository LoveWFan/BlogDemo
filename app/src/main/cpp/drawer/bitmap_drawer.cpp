//
// Created by feibiao.ma on 2020/10/16.
//

#include "bitmap_drawer.h"
#include "../utils/logger.h"
#include <malloc.h>
#include <math.h>
#include <cmath>

BitmapDrawer::BitmapDrawer(int origin_width, int origin_height, void *p) {
    m_origin_width = (float) origin_width;
    m_origin_height = (float) origin_height;
    cst_data = p;
}

BitmapDrawer::~BitmapDrawer() {

}


void BitmapDrawer::Release() {

}

void BitmapDrawer::DoDraw() {
    //创建程序
    CreateProgram();

    //启用顶点的句柄
    glEnableVertexAttribArray(m_vertex_pos_handler);
    glEnableVertexAttribArray(m_texture_pos_handler);
    //设置着色器参数
//    glUniformMatrix4fv(m_vertex_matrix_handler, 1, false, m_matrix, 0);
    glVertexAttribPointer(m_vertex_pos_handler, 3, GL_FLOAT, GL_FALSE, 0, m_vertex_coors);
    glVertexAttribPointer(m_texture_pos_handler, 2, GL_FLOAT, GL_FALSE, 0, m_texture_coors);
    //开始绘制
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
}

void BitmapDrawer::InitVarHandler() {
    m_vertex_pos_handler = glGetAttribLocation(m_program_id, "aPosition");
    m_texture_pos_handler = glGetAttribLocation(m_program_id, "aCoordinate");
    m_texture_handler = glGetUniformLocation(m_program_id, "uTexture");
    //生成纹理
    if (m_texture_id == 0) {
        glGenTextures(1, &m_texture_id);
        LOGI(TAG, "Create texture id : %d, %x", m_texture_id, glGetError())
    }
    ActivateTexture();

    //绑定纹理数据
    if (cst_data != NULL) {
        glTexImage2D(GL_TEXTURE_2D, 0, // level一般为0
                     GL_RGBA, //纹理内部格式
                     m_origin_width, m_origin_height, // 画面宽高
                     0, // 必须为0
                     GL_RGBA, // 数据格式，必须和上面的纹理格式保持一直
                     GL_UNSIGNED_BYTE, // RGBA每位数据的字节数，这里是BYTE​: 1 byte
                     cst_data);// 画面数据
        //释放资源
        free(cst_data);
        cst_data = NULL;
    }

}


void BitmapDrawer::ActivateTexture() {
    GLenum type = GL_TEXTURE_2D;
    //激活指定纹理单元
//    glActiveTexture(GL_TEXTURE0 + index);
    //绑定纹理ID到纹理单元
    glBindTexture(type, m_texture_id);
    //将活动的纹理单元传递到着色器里面
//    glUniform1i(texture_handler, index);
    //配置边缘过渡参数
    glTexParameterf(type, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameterf(type, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    glTexParameteri(type, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameteri(type, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

    /*
     *
     *   glTexParameteri(type, GL_TEXTURE_WRAP_S, GL_MIRRORED_REPEAT);
     *   glTexParameteri(type, GL_TEXTURE_WRAP_T, GL_MIRRORED_REPEAT);
     */
}

const char *BitmapDrawer::GetVertexShader() {
    return "attribute vec4 aPosition;  \n"//顶点坐标
           "attribute vec2 aCoordinate;  \n"//纹理坐标
           "varying vec2 vCoordinate;  \n"//纹理坐标
           "void main()                              \n"
           "{                                        \n"
           "   gl_Position = aPosition;              \n"
           "   vCoordinate = aCoordinate;              \n"
           "}                                        \n";;
}

const char *BitmapDrawer::GetFragmentShader() {
    return "precision mediump float;                     \n"//配置精度
           "uniform sampler2D uTexture;                  \n"
           "varying vec2 vCoordinate;                 \n"
           "void main()                                  \n"
           "{                                            \n"
           "   gl_FragColor = texture2D ( uTexture, vCoordinate);  \n"
           "}                                            \n";
}

void BitmapDrawer::AdjustImageScale() {
    if (m_output_width > 0
        && m_output_height > 0
        && m_origin_width > 0
        && m_origin_height > 0) {
        ResetTextureCoors();
        float ratio1 = m_output_width / m_origin_width;
        float ratio2 = m_output_height / m_origin_height;
        float ratioMax = fmaxf(ratio1, ratio2);
        int imageWidthNew = round(m_origin_width * ratioMax);
        int imageHeightNew = round(m_origin_height * ratioMax);

        float ratioWidth = imageWidthNew / m_output_width;
        float ratioHeight = imageHeightNew / m_output_height;

        float distHorizontal = (1 - 1 / ratioWidth) / 2;
        float distVertical = (1 - 1 / ratioHeight) / 2;
        m_texture_coors[0] = addDistance(m_texture_coors[0], distHorizontal);
        m_texture_coors[1] = addDistance(m_texture_coors[1], distVertical);
        m_texture_coors[2] = addDistance(m_texture_coors[2], distHorizontal);
        m_texture_coors[3] = addDistance(m_texture_coors[3], distVertical);
        m_texture_coors[4] = addDistance(m_texture_coors[4], distHorizontal);
        m_texture_coors[5] = addDistance(m_texture_coors[5], distVertical);
        m_texture_coors[6] = addDistance(m_texture_coors[6], distHorizontal);
        m_texture_coors[7] = addDistance(m_texture_coors[7], distVertical);

    }

}

void BitmapDrawer::OnOutputSizeChanged(int outputWidth, int outputHeight) {
    m_output_width = (float) outputWidth;
    m_output_height = (float) outputHeight;
    AdjustImageScale();
}

float BitmapDrawer::addDistance(const GLfloat coordinate, float distance) {
    return coordinate == 0.0f ? distance : 1 - distance;
}

void BitmapDrawer::ResetTextureCoors() {
    m_texture_coors[0] = 0.0f;
    m_texture_coors[1] = 1.0f;
    m_texture_coors[2] = 1.0f;
    m_texture_coors[3] = 1.0f;
    m_texture_coors[4] = 0.0f;
    m_texture_coors[5] = 0.0f;
    m_texture_coors[6] = 1.0f;
    m_texture_coors[7] = 0.0f;
}
