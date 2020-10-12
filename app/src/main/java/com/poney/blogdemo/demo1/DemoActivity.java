package com.poney.blogdemo.demo1;

import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.poney.blogdemo.R;
import com.poney.blogdemo.demo1.drawer.BitmapDrawer;
import com.poney.blogdemo.demo1.drawer.IDrawer;
import com.poney.blogdemo.demo1.drawer.TriangleDrawer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DemoActivity extends AppCompatActivity {

    @BindView(R.id.gl_surface)
    GLSurfaceView glSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        ButterKnife.bind(this);

        MyGLRender renderer = new MyGLRender();
        renderer.setDrawer(new TriangleDrawer());
//        renderer.setDrawer(new BitmapDrawer(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_default)));
        glSurface.setEGLContextClientVersion(2);
        glSurface.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glSurface.getHolder().setFormat(PixelFormat.RGBA_8888);
        glSurface.setRenderer(renderer);
        glSurface.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    private static class MyGLRender implements GLSurfaceView.Renderer {
        private IDrawer iDrawer;

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
            GLES20.glViewport(0, 0, width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            // 清屏，否则会有画面残留
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            iDrawer.draw();
        }


        public void setDrawer(IDrawer iDrawer) {
            this.iDrawer = iDrawer;
        }


    }
}