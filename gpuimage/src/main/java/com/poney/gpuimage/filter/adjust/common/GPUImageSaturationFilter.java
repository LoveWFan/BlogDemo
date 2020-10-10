package com.poney.gpuimage.filter.adjust.common;

import android.opengl.GLES20;

import com.poney.gpuimage.filter.base.GPUImageAdjustFilter;

/**
 * 饱和度可定义为彩度除以明度，与彩度同样表征彩色偏离同亮度灰色的程度。饱和度是指色彩的鲜艳程度，也称色彩的纯度。
 * <p>
 * 饱和度取决于该色中含色成分和消色成分（灰色）的比例。含色成分越大，饱和度越大；消色成分越大，饱和度越小。
 * <p>
 * 纯的颜色都是高度饱和的，如鲜红，鲜绿。混杂上白色，灰色或其他色调的颜色，是不饱和的颜色，如绛紫，粉红，黄褐等。完全不饱和的颜色根本没有色调，如黑白之间的各种灰色。
 * <p>
 * 简单的说，饱和度就是每个像素色彩本身的鲜艳程度，饱和度越低越接近灰色，越高就越鲜艳。先根据亮度比例计算出灰度值，用灰度值与原色通过饱和度混合就可以得到新的颜色了。
 */
public class GPUImageSaturationFilter extends GPUImageAdjustFilter {
    public static final String CONTRAST_FRAGMENT_SHADER = "" +
            "varying highp vec2 textureCoordinate;\n" +
            " \n" +
            " uniform sampler2D inputImageTexture;\n" +
            " uniform lowp float saturation;\n" +
            "const mediump vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);\n" +
            " \n" +
            " void main()\n" +
            " {\n" +
            "     lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
            "     lowp float luminance = dot(textureColor.rgb, luminanceWeighting);\n" +
            "     lowp vec3 greyScaleColor = vec3(luminance);\n" +
            "     \n" +
            "     gl_FragColor = vec4(mix(greyScaleColor,textureColor.rgb,saturation), textureColor.w);\n" +
            " }";
    private int saturationLocation;
    private float saturation;

    public GPUImageSaturationFilter() {
        this(0.0f);
    }

    public GPUImageSaturationFilter(float saturation) {
        super(NO_FILTER_VERTEX_SHADER, CONTRAST_FRAGMENT_SHADER);
        this.saturation = saturation;
    }

    @Override
    public void onInit() {
        super.onInit();
        saturationLocation = GLES20.glGetUniformLocation(getProgram(), "saturation");
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        setSaturation(saturation);
    }

    public void setSaturation(float range) {
        this.saturation = range;
        setFloat(saturationLocation, saturation);
    }
}
