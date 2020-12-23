package com.poney.blogdemo.demo2

import android.os.Build
import android.os.Bundle
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import com.poney.blogdemo.R
import com.poney.blogdemo.base.camera.Camera1Loader
import com.poney.blogdemo.base.camera.Camera2Loader
import com.poney.blogdemo.base.camera.CameraLoader
import com.poney.blogdemo.base.camera.doOnLayout
import com.poney.encoder.H264MediaCodecEncoder
import kotlinx.android.synthetic.main.activity_camerax.*
import java.io.File


class CameraXActivity : AppCompatActivity() {

    var mMediaCodecEncoder: H264MediaCodecEncoder? = null


    @Volatile
    private var isRecording: Boolean = false;
    private var outFile: File? = null
    private val cameraLoader: CameraLoader by lazy {
        if (Build.VERSION.SDK_INT < 21) {
            Camera1Loader(this)
        } else {
            Camera2Loader(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camerax)
        outFile = File(externalCacheDir, "demo1.yuv")

        surface.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                holder?.let { cameraLoader.setPreview(it) }


            }

        })
        cameraLoader.setOnPreviewFrameListener { data: ByteArray?, width: Int?, height: Int? ->
            if (isRecording) {
                if (mMediaCodecEncoder == null) {
                    mMediaCodecEncoder = H264MediaCodecEncoder(width!!, height!!, 30, externalCacheDir?.absolutePath + File.separator + "demo1.mp4")
                    mMediaCodecEncoder?.startEncoder()
                }
                mMediaCodecEncoder?.putData(data)
            } else {
                endRecord()
            }

        }

        recordButton.setOnClickListener {
            isRecording = !isRecording
            if (isRecording)
                recordButton.setText("停止录制")
            else
                recordButton.setText("开始录制")
        }

    }

    private fun endRecord() {
        mMediaCodecEncoder?.stopEncoder()
        mMediaCodecEncoder = null
    }


    override fun onResume() {
        super.onResume()
        surface.doOnLayout {
            cameraLoader.onResume(it.width, it.height)
        }
    }

    override fun onPause() {
        cameraLoader.onPause()
        mMediaCodecEncoder?.stopEncoder()
        mMediaCodecEncoder = null
        super.onPause()
    }
}