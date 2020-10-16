//
// Created by feibiao.ma on 2020/10/16.
//

#include "base_drawer.h"
#include "../utils/logger.h"
#include <malloc.h>

void BaseDrawer::CreateProgram() {
    if (m_program_id == 0) {
        //创建一个空的OpenGLES程序，注意：需要在OpenGL渲染线程中创建，否则无法渲染
        m_program_id = glCreateProgram();
        LOGI(TAG, "create gl program : %d, %x", m_program_id, glGetError())
        if (glGetError() != GL_NO_ERROR) {
            return;
        }

        GLuint vertexShader = LoadShader(GL_VERTEX_SHADER, GetVertexShader());
        GLuint fragmentShader = LoadShader(GL_FRAGMENT_SHADER, GetFragmentShader());

        //将顶点着色器加入到程序
        glAttachShader(m_program_id, vertexShader);
        //将片元着色器加入到程序中
        glAttachShader(m_program_id, fragmentShader);
        //连接到着色器程序
        glLinkProgram(m_program_id);


        //获取变量句柄
        InitVarHandler();

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }
    //使用OpenGL程序
    if (m_program_id != 0) {
        glUseProgram(m_program_id);
    }
}

GLuint BaseDrawer::LoadShader(GLenum type, const GLchar *shader_code) {
    LOGI(TAG, "Load shader:\n %s", shader_code)
    //根据type创建顶点着色器或者片元着色器
    GLuint shader = glCreateShader(type);
    //将资源加入到着色器中，并编译
    glShaderSource(shader, 1, &shader_code, NULL);
    glCompileShader(shader);

    GLint compiled;
    // 检查编译状态
    glGetShaderiv(shader, GL_COMPILE_STATUS, &compiled);
    if (!compiled) {
        GLint infoLen = 0;

        glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &infoLen);

        if (infoLen > 1) {
            GLchar *infoLog = (GLchar *) malloc(sizeof(GLchar) * infoLen);

            glGetShaderInfoLog(shader, infoLen, NULL, infoLog);
            LOGI(TAG, "Error compiling shader:\n%s\n", infoLog);

            free(infoLog);
        }

        glDeleteShader(shader);
        return 0;
    }
    return shader;
}

BaseDrawer::BaseDrawer() {

}

BaseDrawer::~BaseDrawer() {

}


