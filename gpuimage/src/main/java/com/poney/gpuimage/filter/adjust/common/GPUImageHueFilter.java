package com.poney.gpuimage.filter.adjust.common;

import android.opengl.GLES20;

import com.poney.gpuimage.filter.base.GPUImageAdjustFilter;


/**
 * 明色调(Tint)与暗色调(Shade)。明色调，也有人称为含白度。暗色调，也有人称为含黑度。
 * <p>
 * 在色彩理论中，明色调是一种与白色颜色的混合，这减少了暗度。而暗色调是与黑色的混合，这增加了暗度。色调(Tone)则透过和灰色的混合，或是和明色调和暗色调两者着色的混合。将颜色和任何的中性色混合(包括黑，灰和白色)可降低彩度或视彩度，同时色相保持不变。
 * <p>
 * 色调不是指颜色的性质，是对一幅绘画作品的整体评价。一幅绘画作品虽然可能用了多种颜色，但总体有一种色调，是偏蓝或偏红，是偏暖或偏冷等等。
 * <p>
 * 色调(Hue)描述的是整体的颜色效果，但是在HSV/HLS色彩空间中调整效果不是很理想，这里建议切换到YIQ色彩空间进行计算
 */
public class GPUImageHueFilter extends GPUImageAdjustFilter {
    public static final String CONTRAST_FRAGMENT_SHADER = "" +
            "precision highp float;" +
            "varying highp vec2 textureCoordinate;\n" +
            " \n" +
            "uniform sampler2D inputImageTexture;\n" +
            "uniform mediump float hueAdjust;\n" +
            "const highp  vec4  kRGBToYPrime = vec4 (0.299, 0.587, 0.114, 0.0);\n" +
            "const highp  vec4  kRGBToI     = vec4 (0.595716, -0.274453, -0.321263, 0.0);\n" +
            "const highp  vec4  kRGBToQ     = vec4 (0.211456, -0.522591, 0.31135, 0.0);\n" +
            " \n" +
            "const highp  vec4  kYIQToR   = vec4 (1.0, 0.9563, 0.6210, 0.0);\n" +
            "const highp  vec4  kYIQToG   = vec4 (1.0, -0.2721, -0.6474, 0.0);\n" +
            "const highp  vec4  kYIQToB   = vec4 (1.0, -1.1070, 1.7046, 0.0);\n" +
            " \n" +
            "void main () {\n" +
            "    // Sample the input pixel\n" +
            "    highp vec4 color   = texture2D(inputImageTexture, textureCoordinate);\n" +
            "     \n" +
            "    // Convert to YIQ\n" +
            "    highp float   YPrime  = dot (color, kRGBToYPrime);\n" +
            "    highp float   I      = dot (color, kRGBToI);\n" +
            "    highp float   Q      = dot (color, kRGBToQ);\n" +
            "     \n" +
            "    // Calculate the hue and chroma\n" +
            "    highp float   hue     = atan (Q, I);\n" +
            "    highp float   chroma  = sqrt (I * I + Q * Q);\n" +
            "     \n" +
            "    // Make the user's adjustments\n" +
            "    hue += (-hueAdjust); //why negative rotation?\n" +
            "     \n" +
            "    // Convert back to YIQ\n" +
            "    Q = chroma * sin (hue);\n" +
            "    I = chroma * cos (hue);\n" +
            "     \n" +
            "    // Convert back to RGB\n" +
            "    highp vec4  yIQ = vec4 (YPrime, I, Q, 0.0);\n" +
            "    color.r = dot (yIQ, kYIQToR);\n" +
            "    color.g = dot (yIQ, kYIQToG);\n" +
            "    color.b = dot (yIQ, kYIQToB);\n" +
            "     \n" +
            "    // Save the result\n" +
            "    gl_FragColor = color;\n" +
            " }";
    private int hueLocation;
    private float hue;

    public GPUImageHueFilter() {
        this(90.0f);
    }

    public GPUImageHueFilter(float hue) {
        super(NO_FILTER_VERTEX_SHADER, CONTRAST_FRAGMENT_SHADER);
        this.hue = hue;
    }

    @Override
    public void onInit() {
        super.onInit();
        hueLocation = GLES20.glGetUniformLocation(getProgram(), "hueAdjust");
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        setHue(hue);
    }

    public void setHue(float range) {
        this.hue = range;
        setFloat(hueLocation, hue);
    }
}
