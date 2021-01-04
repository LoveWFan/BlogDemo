//
// Created by poney.ma on 2020/10/28.
//

#ifndef BLOGDEMO_OPENGL_UTILS_H
#define BLOGDEMO_OPENGL_UTILS_H

#include <GLES2/gl2.h>
#include <malloc.h>
#include "logger.h"

class OpenGLUtils {
public:


    static GLuint LoadShader(GLenum type, const GLchar *shader_code) {
        LOGI("OpenGLUtils", "Load shader:\n %s", shader_code)
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
                LOGI("OpenGLUtils", "Error compiling shader:\n%s\n", infoLog);

                free(infoLog);
            }

            glDeleteShader(shader);
            return 0;
        }
        return shader;
    }

    static GLint
    CreateProgram(const GLchar *vertex_shader_code, const GLchar *fragment_shader_code) {
        //创建一个空的OpenGLES程序，注意：需要在OpenGL渲染线程中创建，否则无法渲染
        int m_program_id = glCreateProgram();
        LOGI("OpenGLUtils", "create gl program : %d, %x", m_program_id, glGetError())
        if (glGetError() != GL_NO_ERROR) {
            return 0;
        }

        GLuint vertexShader = LoadShader(GL_VERTEX_SHADER, vertex_shader_code);
        GLuint fragmentShader = LoadShader(GL_FRAGMENT_SHADER, fragment_shader_code);

        //将顶点着色器加入到程序
        glAttachShader(m_program_id, vertexShader);
        //将片元着色器加入到程序中
        glAttachShader(m_program_id, fragmentShader);
        //连接到着色器程序
        glLinkProgram(m_program_id);

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
        //使用OpenGL程序
        if (m_program_id != 0) {
            glUseProgram(m_program_id);
        }

        return m_program_id;
    }

    static void ActivateTexture(GLuint m_texture_id) {
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

    static GLint loadTexture(void *cst_data, int m_origin_width, int m_origin_height) {//生成纹理
        GLuint m_texture_id = 0;
        glGenTextures(1, &m_texture_id);
        LOGI("OpenGLUtils", "Create texture id : %d, %x", m_texture_id, glGetError())
        ActivateTexture(m_texture_id);

        //绑定纹理数据
        if (cst_data != NULL) {
            glTexImage2D(GL_TEXTURE_2D, 0, // level一般为0
                         GL_RGBA, //纹理内部格式
                         m_origin_width, m_origin_height, // 画面宽高
                         0, // 必须为0
                         GL_RGBA, // 数据格式，必须和上面的纹理格式保持一直
                         GL_UNSIGNED_BYTE, // RGBA每位数据的字节数，这里是BYTE​: 1 byte
                         cst_data);// 画面数据
        }

        return m_texture_id;
    }
};


#endif //BLOGDEMO_OPENGL_UTILS_H
