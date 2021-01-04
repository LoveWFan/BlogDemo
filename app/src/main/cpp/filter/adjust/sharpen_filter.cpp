//
// Created by poney.ma on 2020/11/3.
//

#include "sharpen_filter.h"

void SharpenFilter::OnInit() {
    ImageFilter::OnInit();
    m_sharpnessLocation = glGetUniformLocation(m_program_id, "sharpness");
    imageWidthFactorLocation = glGetUniformLocation(m_program_id, "imageWidthFactor");
    imageHeightFactorLocation = glGetUniformLocation(m_program_id, "imageHeightFactor");
    setValue(m_sharpness);
}

const GLchar *SharpenFilter::GetFragmentShader() {
    return ""
           "precision highp float;\n"
           "\n"
           "varying highp vec2 textureCoordinate;\n"
           "varying highp vec2 leftTextureCoordinate;\n"
           "varying highp vec2 rightTextureCoordinate; \n"
           "varying highp vec2 topTextureCoordinate;\n"
           "varying highp vec2 bottomTextureCoordinate;\n"
           "\n"
           "varying highp float centerMultiplier;\n"
           "varying highp float edgeMultiplier;\n"
           "\n"
           "uniform sampler2D inputImageTexture;\n"
           "\n"
           "void main()\n"
           "{\n"
           "    mediump vec3 textureColor = texture2D(inputImageTexture, textureCoordinate).rgb;\n"
           "    mediump vec3 leftTextureColor = texture2D(inputImageTexture, leftTextureCoordinate).rgb;\n"
           "    mediump vec3 rightTextureColor = texture2D(inputImageTexture, rightTextureCoordinate).rgb;\n"
           "    mediump vec3 topTextureColor = texture2D(inputImageTexture, topTextureCoordinate).rgb;\n"
           "    mediump vec3 bottomTextureColor = texture2D(inputImageTexture, bottomTextureCoordinate).rgb;\n"
           "\n"
           "    gl_FragColor = vec4((textureColor * centerMultiplier - (leftTextureColor * edgeMultiplier + rightTextureColor * edgeMultiplier + topTextureColor * edgeMultiplier + bottomTextureColor * edgeMultiplier)), texture2D(inputImageTexture, bottomTextureCoordinate).w);\n"
           "}";;
}


void SharpenFilter::setValue(float sharpness) {
    m_sharpness = sharpness;
    setFloat(m_sharpnessLocation, m_sharpness);
}

const GLchar *SharpenFilter::GetVertexShader() {
    return ""
           "attribute vec4 position;\n"
           "attribute vec4 inputTextureCoordinate;\n"
           "\n"
           "uniform float imageWidthFactor; \n"
           "uniform float imageHeightFactor; \n"
           "uniform float sharpness;\n"
           "\n"
           "varying vec2 textureCoordinate;\n"
           "varying vec2 leftTextureCoordinate;\n"
           "varying vec2 rightTextureCoordinate; \n"
           "varying vec2 topTextureCoordinate;\n"
           "varying vec2 bottomTextureCoordinate;\n"
           "\n"
           "varying float centerMultiplier;\n"
           "varying float edgeMultiplier;\n"
           "\n"
           "void main()\n"
           "{\n"
           "    gl_Position = position;\n"
           "    \n"
           "    mediump vec2 widthStep = vec2(imageWidthFactor, 0.0);\n"
           "    mediump vec2 heightStep = vec2(0.0, imageHeightFactor);\n"
           "    \n"
           "    textureCoordinate = inputTextureCoordinate.xy;\n"
           "    leftTextureCoordinate = inputTextureCoordinate.xy - widthStep;\n"
           "    rightTextureCoordinate = inputTextureCoordinate.xy + widthStep;\n"
           "    topTextureCoordinate = inputTextureCoordinate.xy + heightStep;     \n"
           "    bottomTextureCoordinate = inputTextureCoordinate.xy - heightStep;\n"
           "    \n"
           "    centerMultiplier = 1.0 + 4.0 * sharpness;\n"
           "    edgeMultiplier = sharpness;\n"
           "}";
}

void SharpenFilter::onOutputSizeChanged(int width, int height) {
    setFloat(imageWidthFactorLocation, 1.0f / width);
    setFloat(imageHeightFactorLocation, 1.0f / height);
}
