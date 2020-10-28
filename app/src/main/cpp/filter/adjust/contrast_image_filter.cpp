//
// Created by feibiao.ma on 2020/10/28.
//

#include "contrast_image_filter.h"


void ContrastImageFilter::OnInit() {
    LOGE("MFB", "OnInit")
    ImageFilter::OnInit();
    m_contrastLocation = glGetUniformLocation(m_program_id, "m_contrast");
    setContrast(m_contrast);
}

const GLchar *ContrastImageFilter::GetFragmentShader() {
    return ""
           "varying highp vec2 textureCoordinate;\n"
           " \n"
           " uniform sampler2D inputImageTexture;\n"
           " uniform lowp float m_contrast;\n"
           " \n"
           " void main()\n"
           " {\n"
           "     lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n"
           "     \n"
           "     gl_FragColor = vec4(((textureColor.rgb - vec3(0.5)) * m_contrast + vec3(0.5)), textureColor.w);\n"
           " }";

}

void ContrastImageFilter::setContrast(float contrast) {
    m_contrast = contrast;
    setFloat(m_contrastLocation, contrast);
}
