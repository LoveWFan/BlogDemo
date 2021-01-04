//
// Created by poney.ma on 2020/10/28.
//

#include "exposure_filter.h"

void ExposureFilter::OnInit() {
    ImageFilter::OnInit();
    m_exposureLocation = glGetUniformLocation(m_program_id, "exposure");
    setValue(m_exposure);
}

const GLchar *ExposureFilter::GetFragmentShader() {
    return ""
           " varying highp vec2 textureCoordinate;\n"
           " \n"
           " uniform sampler2D inputImageTexture;\n"
           " uniform highp float exposure;\n"
           " \n"
           " void main()\n"
           " {\n"
           "     highp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n"
           "     \n"
           "     gl_FragColor = vec4(textureColor.rgb * pow(2.0, exposure), textureColor.w);\n"
           " } ";
}

void ExposureFilter::setValue(float exposure) {
    m_exposure = exposure;
    setFloat(m_exposureLocation, m_exposure);
}
