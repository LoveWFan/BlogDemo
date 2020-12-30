package com.poney.ffmpeg.play;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by tingkuo.
 * Date: 2018-08-24
 * Time: 09:35
 */
public class FFVideoView extends SurfaceView implements SurfaceHolder.Callback {

    Surface mSurface;
    private int viewWidth;
    private int viewHeight;

    public FFVideoView(Context context) {
        this(context, null);
    }

    public FFVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public FFVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FFVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        getHolder().addCallback(this);
    }


    public void playVideo(final String videoPath) {
        if (mSurface != null && viewWidth != 0 && viewHeight != 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    FFUtils.playVideo(videoPath, mSurface, viewWidth, viewHeight);
                }
            }).start();
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        holder.setFormat(PixelFormat.RGBA_8888);
        mSurface = holder.getSurface();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        viewWidth = width;
        viewHeight = height;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
}
