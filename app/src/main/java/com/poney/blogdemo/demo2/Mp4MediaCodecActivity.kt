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
import com.poney.ffmpeg.encoder.AACMediaCodecEncoder
import com.poney.ffmpeg.encoder.H264MediaCodecEncoder
import com.poney.ffmpeg.encoder.Mp4MediaCodecRecord
import kotlinx.android.synthetic.main.activity_codec_mp4.*
import kotlinx.android.synthetic.main.activity_ffmpeg_h_a.*
import kotlinx.android.synthetic.main.activity_ffmpeg_h_a.surface
import kotlinx.android.synthetic.main.activity_ffmpeg_h_a.switch_camera
import java.io.File


class Mp4MediaCodecActivity : AppCompatActivity() {

    var mp4MediaCodecRecord: Mp4MediaCodecRecord? = null


    private var previewWidth: Int? = 0
    private var previewHeight: Int? = 0

    @Volatile
    private var isMediaCodecRecording: Boolean = false
    private var isNativeRecording: Boolean = false
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
        setContentView(R.layout.activity_codec_mp4)
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
            previewWidth = width
            previewHeight = height
            if (isMediaCodecRecording) {
                startMediaCodecRecord(width, height, data)
            } else {
                endMediaCodecRecord()
            }

        }

        mp4_record_btn.setOnClickListener {
            if (isNativeRecording)
                return@setOnClickListener
            isMediaCodecRecording = !isMediaCodecRecording
            if (isMediaCodecRecording) {
                mp4_record_btn.setText("Mp4停止")
            } else {
                mp4_record_btn.setText("Mp4开始")
            }

        }


        switch_camera.setOnClickListener {
            cameraLoader.switchCamera()
        }
    }


    private fun startMediaCodecRecord(width: Int?, height: Int?, data: ByteArray?) {
        if (mp4MediaCodecRecord == null) {
            mp4MediaCodecRecord = Mp4MediaCodecRecord(width!!, height!!,
                    AACMediaCodecEncoder.DEFAULT_SAMPLE_RATE_IN_HZ,
                    AACMediaCodecEncoder.DEFAULT_CHANNEL_CONFIG,
                    externalCacheDir?.absolutePath + File.separator + "mediacodec_av_demo.mp4")
            mp4MediaCodecRecord?.start()
        }


    }


    private fun endMediaCodecRecord() {
        mp4MediaCodecRecord?.stop()
        mp4MediaCodecRecord = null
        isMediaCodecRecording = false
    }


    override fun onResume() {
        super.onResume()
        surface.doOnLayout {
            cameraLoader.onResume(it.width, it.height)
        }
    }

    override fun onPause() {
        cameraLoader.onPause()
        mp4MediaCodecRecord?.stop()
        mp4MediaCodecRecord = null
        super.onPause()
    }
}