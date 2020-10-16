package com.poney.gpuimage.filter.adjust.common;

import android.opengl.GLES20;

import com.poney.gpuimage.filter.base.GPUImageAdjustFilter;

/**
 *图像锐化(image sharpening)是补偿图像的轮廓，增强图像的边缘及灰度跳变的部分，使图像变得清晰，分为空域处理和频域处理两类。
 *
 * 图像锐化是为了突出图像上地物的边缘、轮廓，或某些线性目标要素的特征。这种滤波方法提高了地物边缘与周围像元之间的反差，因此也被称为边缘增强。
 *
 * 简单的说，我们通过周围像素之间的对比，提高对比度让边缘轮廓更清晰。根据四周像素点的颜色和锐化程度改变中心点和四周的差值。
 */
public class GPUImageSharpenFilter extends GPUImageAdjustFilter {
    public static final String VERTEX_SHADER = "" +
            "attribute vec4 position;\n" +
            "attribute vec4 inputTextureCoordinate;\n" +
            " \n" +
            "uniform float imageWidthFactor;\n" +
            "uniform float imageHeightFactor;\n" +
            "uniform float sharpness;\n" +
            " \n" +
            "varying vec2 textureCoordinate;\n" +
            "varying vec2 leftTextureCoordinate;\n" +
            "varying vec2 rightTextureCoordinate;\n" +
            "varying vec2 topTextureCoordinate;\n" +
            "varying vec2 bottomTextureCoordinate;\n" +
            " \n" +
            "varying float centerMultiplier;\n" +
            "varying float  edgeMultiplier;\n" +
            " \n" +
            "void main()\n" +
            "{\n" +
            "    gl_Position = position;\n" +
            "    \n" +
            "    mediump vec2 widthStep = vec2(imageWidthFactor, 0.0);\n" +
            "    mediump vec2 heightStep = vec2(0.0, imageHeightFactor);\n" +
            "    \n" +
            "    textureCoordinate = inputTextureCoordinate.xy;\n" +
            "    leftTextureCoordinate = inputTextureCoordinate.xy - widthStep;\n" +
            "    rightTextureCoordinate = inputTextureCoordinate.xy + widthStep;\n" +
            "    topTextureCoordinate = inputTextureCoordinate.xy - heightStep;\n" +
            "    bottomTextureCoordinate = inputTextureCoordinate.xy + heightStep;\n" +

            "    \n" +
            "    centerMultiplier = 1.0 + 4.0 * sharpness;\n" +
            "    edgeMultiplier = sharpness;\n" +
            "}";

    public static final String CONTRAST_FRAGMENT_SHADER = "" +
            "precision highp float;\n" +
            "varying highp vec2 textureCoordinate;\n" +
            "varying vec2 leftTextureCoordinate;\n" +
            "varying vec2 rightTextureCoordinate;\n" +
            "varying vec2 topTextureCoordinate;\n" +
            "varying vec2 bottomTextureCoordinate;\n" +
            " \n" +
            "varying float centerMultiplier;\n" +
            "varying float  edgeMultiplier;\n" +
            " \n" +
            " uniform sampler2D inputImageTexture;\n" +
            " \n" +
            " void main()\n" +
            " {\n" +
            "    mediump vec3 textureColor = texture2D(inputImageTexture, textureCoordinate).rgb;\n" +
            "    mediump vec3 leftTextureColor = texture2D(inputImageTexture, leftTextureCoordinate).rgb;\n" +
            "    mediump vec3 rightTextureColor = texture2D(inputImageTexture, rightTextureCoordinate).rgb;\n" +
            "    mediump vec3 topTextureColor = texture2D(inputImageTexture, topTextureCoordinate).rgb;\n" +
            "    mediump vec3 bottomTextureColor = texture2D(inputImageTexture, bottomTextureCoordinate).rgb;\n" +

            "     \n" +
            "    gl_FragColor = vec4((textureColor * centerMultiplier - (leftTextureColor * edgeMultiplier + rightTextureColor * edgeMultiplier + topTextureColor * edgeMultiplier + bottomTextureColor * edgeMultiplier)), texture2D(inputImageTexture, bottomTextureCoordinate).w);\n" +

            " }";
    private int sharpnessLocation;
    private float sharpness;
    private int imageWidthFactorLocation;
    private int imageHeightFactorLocation;

    public GPUImageSharpenFilter() {
        this(0.0f);
    }

    public GPUImageSharpenFilter(float sharpness) {
        super(VERTEX_SHADER, CONTRAST_FRAGMENT_SHADER);
        this.sharpness = sharpness;
    }

    @Override
    public void onInit() {
        super.onInit();
        sharpnessLocation = GLES20.glGetUniformLocation(getProgram(), "sharpness");
        imageWidthFactorLocation = GLES20.glGetUniformLocation(getProgram(), "imageWidthFactor");
        imageHeightFactorLocation = GLES20.glGetUniformLocation(getProgram(), "imageHeightFactor");
    }

    @Override
    public void onOutputSizeChanged(int width, int height) {
        super.onOutputSizeChanged(width, height);
        setFloat(imageWidthFactorLocation, 1.0f / width);
        setFloat(imageHeightFactorLocation, 1.0f / height);
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        setSharpness(sharpness);
    }

    public void setSharpness(float range) {
        this.sharpness = range;
        setFloat(sharpnessLocation, sharpness);
    }
}
