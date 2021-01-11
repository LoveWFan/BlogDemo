package com.poney.blogdemo.demo2

import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import com.poney.blogdemo.R
import com.poney.ffmpeg.audio.AudioRecordLoader
import com.poney.blogdemo.base.camera.Camera1Loader
import com.poney.blogdemo.base.camera.Camera2Loader
import com.poney.blogdemo.base.camera.CameraLoader
import com.poney.blogdemo.base.camera.doOnLayout
import com.poney.ffmpeg.encoder.AACMediaCodecEncoder
import com.poney.ffmpeg.encoder.H264FFMPEGEncoder
import kotlinx.android.synthetic.main.activity_ffmpeg_h_a.*
import java.io.File


class H264AACFFMpegActivity : AppCompatActivity() {


    var mH264FFMPEGEncoder: H264FFMPEGEncoder = H264FFMPEGEncoder()

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

    private val recordLoader: AudioRecordLoader by lazy {
        AudioRecordLoader(MediaRecorder.AudioSource.MIC,
                AACMediaCodecEncoder.DEFAULT_SAMPLE_RATE_IN_HZ,
                AACMediaCodecEncoder.DEFAULT_CHANNEL_CONFIG,
                AACMediaCodecEncoder.DEFAULT_ENCODING,
                AudioRecord.getMinBufferSize(AACMediaCodecEncoder.DEFAULT_SAMPLE_RATE_IN_HZ, AACMediaCodecEncoder.DEFAULT_CHANNEL_CONFIG, AACMediaCodecEncoder.DEFAULT_ENCODING),
                externalCacheDir?.absolutePath + File.separator + "mediacodec_demo.aac")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ffmpeg_h_a)
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

            mH264FFMPEGEncoder.onPreviewFrame(data, width!!, height!!)
        }

        native_record_btn.setOnClickListener {
            if (isMediaCodecRecording)
                return@setOnClickListener
            isNativeRecording = !isNativeRecording
            if (isNativeRecording) {
                native_record_btn.setText("FFMPEG停止")
                startNativeRecord()
            } else {
                native_record_btn.setText("FFMPEG开始")
                endNativeRecord()
            }

        }

        switch_camera.setOnClickListener {
            cameraLoader.switchCamera()
        }
    }

    private fun startNativeRecord() {
        mH264FFMPEGEncoder.encodeMP4Start(externalCacheDir?.absolutePath + File.separator + "native_demo.mp4", previewWidth!!, previewHeight!!)
    }

    private fun endNativeRecord() {
        mH264FFMPEGEncoder.encodeMP4Stop()
    }


    override fun onResume() {
        super.onResume()
        surface.doOnLayout {
            cameraLoader.onResume(it.width, it.height)
        }
    }

    override fun onPause() {
        cameraLoader.onPause()
        super.onPause()
    }
}