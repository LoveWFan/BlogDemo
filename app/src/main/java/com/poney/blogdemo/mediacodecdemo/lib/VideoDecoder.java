package com.poney.blogdemo.mediacodecdemo.lib;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.nio.ByteBuffer;

/**
 * @author poney.ma
 * @project BlogDemo
 * @package_name com.poney.blogdemo.mediacodecdemo.lib
 * @date 2021/1/5
 */
public class VideoDecoder extends BaseDecoder {
    private final String TAG = "VideoDecoder";
    private SurfaceView mSurfaceView;
    private Surface mSurface;

    public VideoDecoder(String filPath, SurfaceView surfaceView, Surface surface) {
        super(filPath);
        this.mSurfaceView = surfaceView;
        this.mSurface = surface;
    }

    @Override
    protected void doneDecode() {

    }

    @Override
    protected void render(ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) {
        Log.i(TAG, "VideoDecoder:" + bufferInfo.size);
    }

    @Override
    protected boolean configCodec(MediaCodec codec, MediaFormat format) {
        if (mSurface != null) {
            codec.configure(format, mSurface, null, 0);
            notifyDecode();
        } else if (mSurfaceView.getHolder().getSurface() != null) {
            mSurface = mSurfaceView.getHolder().getSurface();
            configCodec(codec, format);
        } else {
            mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback2() {
                @Override
                public void surfaceRedrawNeeded(SurfaceHolder holder) {

                }

                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    mSurface = holder.getSurface();
                    configCodec(codec, format);
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {

                }
            });
            return false;
        }
        return true;
    }

    @Override
    protected boolean initRender() {
        return true;
    }

    @Override
    protected void initSpecParams(MediaFormat format) {

    }

    @Override
    protected IExtractor initExtractor(String filePath) {
        return new VideoExtractor(filePath);
    }

    @Override
    protected boolean check() {
        if (mSurfaceView == null && mSurface == null) {
            Log.w(TAG, "SurfaceView和Surface都为空，至少需要一个不为空");
            if (mStateListener != null)
                mStateListener.decoderError(this, "显示器为空");
            return false;
        }
        return true;
    }
}
