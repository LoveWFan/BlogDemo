package com.poney.blogdemo.demo1.drawer

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class TriangleDrawer : IDrawer {
    //顶点坐标
    private val mVertexCoors = floatArrayOf(
            -1f, -1f,
            1f, -1f,
            0f, 1f
    )


    //OpenGL程序ID
    private var mProgram: Int = -1

    // 顶点坐标接收者
    private var mVertexPosHandler: Int = -1


    private lateinit var mVertexBuffer: FloatBuffer

    init {
        //【步骤1: 初始化顶点坐标】
        initPos()
    }

    private fun initPos() {
        val bb = ByteBuffer.allocateDirect(mVertexCoors.size * 4)
        bb.order(ByteOrder.nativeOrder())
        //将坐标数据转换为FloatBuffer，用以传入给OpenGL ES程序
        mVertexBuffer = bb.asFloatBuffer()
        mVertexBuffer.put(mVertexCoors)
        mVertexBuffer.position(0)
    }


    override fun draw() {
        //【步骤2: 创建、编译并启动OpenGL着色器】
        createGLPrg()
        //【步骤3: 开始渲染绘制】
        doDraw()

    }

    private fun createGLPrg() {
        if (mProgram == -1) {
            val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, getVertexShader())
            val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, getFragmentShader())

            //创建OpenGL ES程序，注意：需要在OpenGL渲染线程中创建，否则无法渲染
            mProgram = GLES20.glCreateProgram()
            //将顶点着色器加入到程序
            GLES20.glAttachShader(mProgram, vertexShader)
            //将片元着色器加入到程序中
            GLES20.glAttachShader(mProgram, fragmentShader)
            //连接到着色器程序
            GLES20.glLinkProgram(mProgram)

            mVertexPosHandler = GLES20.glGetAttribLocation(mProgram, "aPosition")
        }
        //使用OpenGL程序
        GLES20.glUseProgram(mProgram)
    }

    private fun doDraw() {
        //启用顶点的句柄
        GLES20.glEnableVertexAttribArray(mVertexPosHandler)
        //设置着色器参数
        GLES20.glVertexAttribPointer(mVertexPosHandler, 2, GLES20.GL_FLOAT, false, 0, mVertexBuffer)
        //开始绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 3)
    }

    override fun release() {
        GLES20.glDisableVertexAttribArray(mVertexPosHandler)
        GLES20.glDeleteProgram(mProgram)
    }


    private fun getVertexShader(): String {
        return "attribute vec4 aPosition;" +
                "void main() {" +
                "  gl_Position = aPosition;" +
                "}"
    }

    private fun getFragmentShader(): String {
        return "precision mediump float;" +
                "void main() {" +
                "  gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);" +
                "}"
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        //根据type创建顶点着色器或者片元着色器
        val shader = GLES20.glCreateShader(type)
        //将资源加入到着色器中，并编译
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)

        return shader
    }
}