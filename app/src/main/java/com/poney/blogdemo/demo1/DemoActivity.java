package com.poney.blogdemo.demo1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.poney.blogdemo.R;
import com.poney.blogdemo.demo1.filter.base.GPUImageFilterType;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DemoActivity extends AppCompatActivity {
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @BindView(R.id.gl_surface)
    GLSurfaceView glSurface;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.fragment_radio_invert)
    RadioButton fragmentRadioInvert;
    @BindView(R.id.fragment_radio_contrast)
    RadioButton fragmentRadioContrast;
    @BindView(R.id.fragment_radio_exposure)
    RadioButton fragmentRadioExposure;
    @BindView(R.id.fragment_radio_saturation)
    RadioButton fragmentRadioSaturation;
    @BindView(R.id.fragment_radio_sharpness)
    RadioButton fragmentRadioSharpness;
    @BindView(R.id.fragment_radio_bright)
    RadioButton fragmentRadioBright;
    @BindView(R.id.fragment_radio_hue)
    RadioButton fragmentRadioHue;
    @BindView(R.id.fragment_adjust_radiogroup)
    RadioGroup fragmentAdjustRadiogroup;
    @BindView(R.id.filter_adjust)
    LinearLayout filterAdjust;
    private MyGLRender renderer;
    private GPUImageFilterType imageFilterType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        renderer = new MyGLRender();
//        renderer.setDrawer(createTriangleDrawer());
        glSurface.setEGLContextClientVersion(2);
        glSurface.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glSurface.getHolder().setFormat(PixelFormat.RGBA_8888);
        glSurface.setRenderer(renderer);
        glSurface.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        fragmentAdjustRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == -1) {
                    seekBar.setVisibility(View.GONE);
                    return;
                }

                seekBar.setVisibility(View.VISIBLE);
                //image adjust filter

                imageFilterType = GPUImageFilterType.NONE;

                if (checkedId == R.id.fragment_radio_default) {
                    seekBar.setVisibility(View.INVISIBLE);
                    imageFilterType = GPUImageFilterType.NONE;
                } else if (checkedId == R.id.fragment_radio_invert) {
                    seekBar.setVisibility(View.INVISIBLE);
                    imageFilterType = GPUImageFilterType.INVERT;
                } else if (checkedId == R.id.fragment_radio_contrast) {
                    imageFilterType = GPUImageFilterType.CONTRAST;
                } else if (checkedId == R.id.fragment_radio_saturation) {
                    imageFilterType = GPUImageFilterType.SATURATION;
                } else if (checkedId == R.id.fragment_radio_exposure) {
                    imageFilterType = GPUImageFilterType.EXPOSURE;
                } else if (checkedId == R.id.fragment_radio_sharpness) {
                    imageFilterType = GPUImageFilterType.SHARPEN;
                } else if (checkedId == R.id.fragment_radio_bright) {
                    imageFilterType = GPUImageFilterType.BRIGHTNESS;
                } else if (checkedId == R.id.fragment_radio_hue) {
                    imageFilterType = GPUImageFilterType.HUE;
                }
                switchToFilter(imageFilterType.ordinal(), createFilterByTypeNative(imageFilterType.ordinal()));

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        renderer.adjust(progress, imageFilterType);
                        glSurface.requestRender();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

            }
        });

    }

    private void switchToFilter(int filterType, long filterNative) {
        if (renderer.getFilter() != filterNative) {
            renderer.setFilter(filterType, filterNative);
            glSurface.requestRender();
        }

    }


    private class MyGLRender implements GLSurfaceView.Renderer {
        private long render = -1;
        private long filter;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            render = createBitmapRender(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_qxx));
            GLES20.glClearColor(0f, 0f, 0f, 0f);

            //------开启混合，即半透明---------
            // 开启很混合模式
            GLES20.glEnable(GLES20.GL_BLEND);
            // 配置混合算法
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            //------------------------------
        }


        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
            if (render != -1) {
                onOutputSizeChanged(render, width, height);
            }

        }

        @Override
        public void onDrawFrame(GL10 gl) {
            // 清屏，否则会有画面残留
            if (render != -1) {
                drawBitmap(render);
            }

        }


        public void release() {
            if (render != -1) {
                releaseNative(render);
            }
        }

        public void setFilter(int filterType, long filter) {
            if (render != -1) {
                this.filter = filter;
                setFilterNative(render, filterType, this.filter);
            }
        }

        public long getFilter() {
            return filter;
        }

        public void adjust(int progress, GPUImageFilterType gpuImageFilterType) {
            float value = 0;
            switch (gpuImageFilterType) {
                case CONTRAST:
                    value = range(progress, 0.0f, 2.0f);
                    break;
                case BRIGHTNESS:
                    value = range(progress, -1.0f, 1.0f);
                    break;
                case EXPOSURE:
                    value = range(progress, -2.0f, 2.0f);
                    break;
            }

            adjustFilterProgressNative(gpuImageFilterType.ordinal(), filter, value);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        renderer.release();
    }

    static float range(int percentage, float start, float end) {
        return (end - start) * percentage / 100.0f + start;
    }

    static int range(int percentage, int start, int end) {
        return (end - start) * percentage / 100 + start;
    }

    public native void releaseNative(long render);

    public native long createBitmapRender(Bitmap bitmap);

    public native void onOutputSizeChanged(long render, int width, int height);

    public native void drawBitmap(long render);

    public native void setFilterNative(long render, int filterType, long filter);

    public native long createFilterByTypeNative(int filterType);

    public native void adjustFilterProgressNative(int filterType, long filter, float value);
}