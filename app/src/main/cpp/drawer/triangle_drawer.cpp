//
// Created by poney.ma on 2020/10/12.
//

#include "triangle_drawer.h"
#include "../utils/logger.h"

TriangleDrawer::TriangleDrawer() {

}

TriangleDrawer::~TriangleDrawer() {

}

void TriangleDrawer::OnInit() {
    //创建程序
    m_program_id = OpenGLUtils::CreateProgram(GetVertexShader(), GetFragmentShader());
    m_vertex_pos_handler = glGetAttribLocation(m_program_id, "aPosition");
}


void TriangleDrawer::Release() {
    glDisableVertexAttribArray(m_vertex_pos_handler);
    glBindTexture(GL_TEXTURE_2D, 0);
    glDeleteProgram(m_program_id);
}

void TriangleDrawer::DoDraw(int textureId, void *vertexPos, void *texturePos) {
    //启用顶点的句柄
    glEnableVertexAttribArray(m_vertex_pos_handler);
    glVertexAttribPointer(m_vertex_pos_handler, 3, GL_FLOAT, GL_FALSE, 0, m_vertex_coors);
    //开始绘制
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 3);

}

const char *TriangleDrawer::GetVertexShader() {
    return "attribute vec4 aPosition;  \n"
           "void main()                              \n"
           "{                                        \n"
           "   gl_Position = aPosition;              \n"
           "}                                        \n";;
}

const char *TriangleDrawer::GetFragmentShader() {
    return "precision mediump float;                     \n"
           "void main()                                  \n"
           "{                                            \n"
           "   gl_FragColor = vec4 ( 1.0, 0.0, 0.0, 1.0 );  \n"
           "}                                            \n";
}