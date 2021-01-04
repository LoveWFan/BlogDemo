//
// Created by poney.ma on 2020/10/28.
//

#include "image_filter.h"

void ImageFilter::setFloat(int location, float floatValue) {
    glUniform1f(location, floatValue);
}

void ImageFilter::OnInit() {
    if (!isInitialized) {
        m_program_id = OpenGLUtils::CreateProgram(GetVertexShader(), GetFragmentShader());
        m_vertex_pos_handler = glGetAttribLocation(m_program_id, "position");
        m_texture_pos_handler = glGetAttribLocation(m_program_id, "inputTextureCoordinate");
        m_texture_handler = glGetUniformLocation(m_program_id, "inputImageTexture");
        isInitialized = true;
    }

}

void ImageFilter::Release() {
    isInitialized = false;
    glDeleteProgram(m_program_id);
}

void ImageFilter::DoDraw(int textureId, void *vertexPos, void *texturePos) {
    //启用顶点的句柄
    glEnableVertexAttribArray(m_vertex_pos_handler);
    glEnableVertexAttribArray(m_texture_pos_handler);
    //设置着色器参数
//    glUniformMatrix4fv(m_vertex_matrix_handler, 1, false, m_matrix, 0);
    glVertexAttribPointer(m_vertex_pos_handler, 3, GL_FLOAT, GL_FALSE, 0, vertexPos);
    glVertexAttribPointer(m_texture_pos_handler, 2, GL_FLOAT, GL_FALSE, 0, texturePos);
    //开始绘制
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
}

const GLchar *ImageFilter::GetVertexShader() {
    return
            "attribute vec4 position;\n"
            "attribute vec4 inputTextureCoordinate;\n"
            " \n"
            "varying vec2 textureCoordinate;\n"
            " \n"
            "void main()\n"
            "{\n"
            "    gl_Position = position;\n"
            "    textureCoordinate = inputTextureCoordinate.xy;\n"
            "}";
}

const GLchar *ImageFilter::GetFragmentShader() {

    return "precision mediump float;\n"
           "varying highp vec2 textureCoordinate;\n"
           " \n"
           "uniform sampler2D inputImageTexture;\n"
           " \n"
           "void main()\n"
           "{\n"
           "     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n"
           "}";
}

