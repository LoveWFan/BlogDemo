package com.poney.blogdemo.demo1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

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
    private long iDrawer = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        MyGLRender renderer = new MyGLRender();
//        renderer.setDrawer(createTriangleDrawer());
        iDrawer = createBitmapDrawer(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_qxx));
        renderer.setDrawer(iDrawer);
        glSurface.setEGLContextClientVersion(2);
        glSurface.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glSurface.getHolder().setFormat(PixelFormat.RGBA_8888);
        glSurface.setRenderer(renderer);
        glSurface.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }


    private class MyGLRender implements GLSurfaceView.Renderer {
        private long iDrawer = -1;
        private int outWidth;
        private int outHeight;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
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
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            // 清屏，否则会有画面残留
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            if (iDrawer != -1) {
                onOutputSizeChanged(iDrawer, outWidth, outHeight);
                drawBitmap(iDrawer);
            }

        }


        public void setDrawer(long iDrawer) {
            this.iDrawer = iDrawer;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iDrawer != -1)
            release(iDrawer);
    }

    public native void release(long drawer);

    public native long createTriangleDrawer();

    public native void drawTriangle(long drawer);

    public native long createBitmapDrawer(Bitmap bitmap);

    public native void onOutputSizeChanged(long drawer, int width, int height);

    public native void drawBitmap(long drawer);
}