//
// Created by poney.ma on 2020/10/29.
//

#include "saturation_filter.h"

void SaturationFilter::OnInit() {
    ImageFilter::OnInit();
    m_saturationLocation = glGetUniformLocation(m_program_id, "saturation");
    setValue(m_saturation);
}

const GLchar *SaturationFilter::GetFragmentShader() {
    return ""
           " varying highp vec2 textureCoordinate;\n"
           " \n"
           " uniform sampler2D inputImageTexture;\n"
           " uniform lowp float saturation;\n"
           " \n"
           " // Values from \"Graphics Shaders: Theory and Practice\" by Bailey and Cunningham\n"
           " const mediump vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);\n"
           " \n"
           " void main()\n"
           " {\n"
           "    lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n"
           "    lowp float luminance = dot(textureColor.rgb, luminanceWeighting);\n"
           "    lowp vec3 greyScaleColor = vec3(luminance);\n"
           "    \n"
           "    gl_FragColor = vec4(mix(greyScaleColor, textureColor.rgb, saturation), textureColor.w);\n"
           "     \n"
           " }";;
}

void SaturationFilter::setValue(float saturation) {
    m_saturation = saturation;
    setFloat(m_saturationLocation, m_saturation);
}
