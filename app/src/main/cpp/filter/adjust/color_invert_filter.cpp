//
// Created by poney.ma on 2020/10/28.
//

#include "color_invert_filter.h"


const GLchar *ColorInvertImageFilter::GetVertexShader() {
    return ImageFilter::GetVertexShader();
}

const GLchar *ColorInvertImageFilter::GetFragmentShader() {
    return ""
           "varying highp vec2 textureCoordinate;\n"
           "\n"
           "uniform sampler2D inputImageTexture;\n"
           "\n"
           "void main()\n"
           "{\n"
           "    lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n"
           "    \n"
           "    gl_FragColor = vec4((1.0 - textureColor.rgb), textureColor.w);\n"
           "}";;
}
