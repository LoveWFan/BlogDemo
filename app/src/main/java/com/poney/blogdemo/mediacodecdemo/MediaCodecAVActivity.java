package com.poney.blogdemo.mediacodecdemo;

import android.os.Bundle;
import android.os.Environment;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.poney.blogdemo.R;
import com.poney.blogdemo.mediacodecdemo.lib.AudioDecoder;
import com.poney.blogdemo.mediacodecdemo.lib.VideoDecoder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * MediaCodec 音视频同步播放
 */
public class MediaCodecAVActivity extends AppCompatActivity implements SurfaceHolder.Callback2 {

    @BindView(R.id.sfv)
    SurfaceView sfv;
    @BindView(R.id.pause_resume)
    Button pauseResume;
    private VideoDecoder mVideoDecoder;
    private AudioDecoder mAudioDecoder;

    private int playState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_codec_a_v);
        ButterKnife.bind(this);
        sfv.getHolder().addCallback(this);
    }

    private void initPlayer(Surface surface) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mvtest.mp4";
        String remoteUrl = "https://raw.githubusercontent.com/LoveWFan/BlogDemo/master/source/mvtest.mp4";
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        //创建视频解码器
        mVideoDecoder = new VideoDecoder(remoteUrl, sfv, surface);
        executorService.execute(mVideoDecoder);
        mAudioDecoder = new AudioDecoder(remoteUrl);
        executorService.execute(mAudioDecoder);

        //播放
        mVideoDecoder.resume();
        mAudioDecoder.resume();
    }

    @Override
    public void surfaceRedrawNeeded(SurfaceHolder holder) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initPlayer(holder.getSurface());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopPlayer();
    }

    private void stopPlayer() {
        mVideoDecoder.release();
        mAudioDecoder.release();
    }

    @OnClick(R.id.pause_resume)
    public void onViewClicked() {
        if (playState == 0) {//播放
            playState = 1;
            pauseResume.setText("PLAY");
            mVideoDecoder.pause();
            mAudioDecoder.pause();
        } else if (playState == 1) {//暂停
            playState = 0;
            pauseResume.setText("PAUSE");
            mVideoDecoder.resume();
            mAudioDecoder.resume();
        }
    }
}