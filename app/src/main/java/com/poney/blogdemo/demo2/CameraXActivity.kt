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
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_camerax.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class CameraXActivity : AppCompatActivity() {
    private var outFile: File? = null;
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
                holder?.let { cameraLoader.setPreview(it) };
            }

        })
        cameraLoader.setOnPreviewFrameListener { data: ByteArray?, width: Int?, height: Int? ->
            Observable.create<Void> {
                var fos: FileOutputStream? = null
                try {
                    fos = FileOutputStream(outFile, true)
                    fos.write(data) // 使用文件输出流写出到文件
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    try {
                        fos?.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
        }
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