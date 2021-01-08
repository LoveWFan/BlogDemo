package com.poney.blogdemo.demo2

import android.R.attr
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import com.poney.blogdemo.R
import com.poney.blogdemo.base.audio.AudioRecordLoader
import com.poney.blogdemo.base.camera.Camera1Loader
import com.poney.blogdemo.base.camera.Camera2Loader
import com.poney.blogdemo.base.camera.CameraLoader
import com.poney.blogdemo.base.camera.doOnLayout
import com.poney.ffmpeg.encoder.AACMediaCodecEncoder
import com.poney.ffmpeg.encoder.H264FFMPEGEncoder
import com.poney.ffmpeg.encoder.H264MediaCodecEncoder
import kotlinx.android.synthetic.main.activity_camerax.*
import java.io.File


class CameraXActivity : AppCompatActivity() {

    var mMediaCodecEncoder: H264MediaCodecEncoder? = null

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
            previewWidth = width
            previewHeight = height
            if (isMediaCodecRecording) {
                startMediaCodecRecord(width, height, data)
            } else {
                endMediaCodecRecord()
            }

            mH264FFMPEGEncoder.onPreviewFrame(data, width!!, height!!)
        }

        media_codec_record_btn.setOnClickListener {
            if (isNativeRecording)
                return@setOnClickListener
            isMediaCodecRecording = !isMediaCodecRecording
            if (isMediaCodecRecording) {
                recordLoader.startRecord()
                media_codec_record_btn.setText("MediaCodec停止")
            } else {
                recordLoader.stopRecord()
                media_codec_record_btn.setText("MediaCodec开始")
            }

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

    private fun startMediaCodecRecord(width: Int?, height: Int?, data: ByteArray?) {
        if (mMediaCodecEncoder == null) {
            mMediaCodecEncoder = H264MediaCodecEncoder(width!!, height!!, 30, externalCacheDir?.absolutePath + File.separator + "mediacodec_demo.h264")
            mMediaCodecEncoder?.startEncoder()
        }
        val nv12 = ByteArray((width!! * height!! * 1.5).toInt())

        NV21ToYUV420(data, nv12, width, height);

        //NV21ToYUV420(data,nv12,width,height);

        mMediaCodecEncoder?.putData(nv12)
    }

    private fun NV21ToNV12(nv21: ByteArray?, nv12: ByteArray?, width: Int, height: Int) {
        if (nv21 == null || nv12 == null) return
        val framesize = width * height
        var i = 0
        var j = 0
        System.arraycopy(nv21, 0, nv12, 0, framesize)
        i = 0
        while (i < framesize) {
            nv12[i] = nv21[i]
            i++
        }
        j = 0
        while (j < framesize / 2) {
            nv12[framesize + j - 1] = nv21[j + framesize]
            j += 2
        }
        j = 0
        while (j < framesize / 2) {
            nv12[framesize + j] = nv21[j + framesize - 1]
            j += 2
        }
    }

    private fun NV21ToYUV420(nv21: ByteArray?, yuv420: ByteArray?, width: Int, height: Int) {
        if (nv21 == null || yuv420 == null) return
        val framesize = width * height
        var i = 0
        var j = 0
        System.arraycopy(nv21, 0, yuv420, 0, framesize)
        i = 0
        while (i < framesize / 4) {
            yuv420[framesize + framesize / 4 + i] = nv21[i * 2 + framesize]
            i++
        }
        j = 0
        while (j < framesize / 4) {
            yuv420[framesize + j] = nv21[j * 2 + 1 + framesize]
            j++
        }
    }

    private fun endMediaCodecRecord() {
        mMediaCodecEncoder?.stopEncoder()
        mMediaCodecEncoder = null
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
        mMediaCodecEncoder?.stopEncoder()
        mMediaCodecEncoder = null
        super.onPause()
    }
}