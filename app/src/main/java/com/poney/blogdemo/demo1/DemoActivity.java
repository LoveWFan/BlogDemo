package com.poney.blogdemo.demo1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.poney.blogdemo.R;

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
    private MyGLRender renderer;

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
    }

    public void onViewClicked(View view) {
        renderer.setFilter(1);
        glSurface.requestRender();
    }

    private class MyGLRender implements GLSurfaceView.Renderer {
        private long render = -1;
        private int outWidth;
        private int outHeight;

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
            this.outWidth = width;
            this.outHeight = height;
            GLES20.glViewport(0, 0, this.outWidth, this.outHeight);
            if (render != -1) {
                onOutputSizeChanged(render, outWidth, outHeight);
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

        public void setFilter(int filterType) {
            if (render != -1) {
                setFilterNative(render, filterType);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        renderer.release();
    }

    public native void releaseNative(long render);

    public native long createBitmapRender(Bitmap bitmap);

    public native void onOutputSizeChanged(long render, int width, int height);

    public native void drawBitmap(long render);

    public native void setFilterNative(long render, int filterType);
}