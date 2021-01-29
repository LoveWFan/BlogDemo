package com.poney.blogdemo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.poney.blogdemo.demo1.DemoActivity;
import com.poney.blogdemo.demo1.EGLDemoActivity;
import com.poney.blogdemo.demo2.H264AACFFMpegActivity;
import com.poney.blogdemo.demo2.H264AACMediaCodecActivity;
import com.poney.blogdemo.demo2.H264AsyncMediaCodecActivity;
import com.poney.blogdemo.demo2.Mp4MediaCodecActivity;
import com.poney.blogdemo.ffplaydemo.FFPlayActivity;
import com.poney.blogdemo.mediacodecdemo.play.MediaCodecAVActivity;
import com.poney.blogdemo.mediacodecdemo.play.MediaCodecAVSyncActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        requestPermissions();
    }

    private void requestPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        Disposable disposable = rxPermissions.request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO)
                .subscribe(permission -> {
                    if (permission) {
                    } else {
                        Toast.makeText(MainActivity.this, "请授予相关权限", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @OnClick({R.id.gLSurfaceView, R.id.egl, R.id.audio_video_h_a, R.id.video_h_async, R.id.audio_video_mp4,
            R.id.audio_video_ffmpeg_h_a, R.id.ff_play, R.id.media_codec_play_av, R.id.media_codec_play_avsync})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.gLSurfaceView:
                startActivity(new Intent(this, DemoActivity.class));
                break;
            case R.id.egl:
                startActivity(new Intent(this, EGLDemoActivity.class));
                break;
            case R.id.audio_video_h_a:
                startActivity(new Intent(this, H264AACMediaCodecActivity.class));
                break;
            case R.id.video_h_async:
                startActivity(new Intent(this, H264AsyncMediaCodecActivity.class));
                break;
            case R.id.audio_video_mp4:
                startActivity(new Intent(this, Mp4MediaCodecActivity.class));
                break;
            case R.id.audio_video_ffmpeg_h_a:
                startActivity(new Intent(this, H264AACFFMpegActivity.class));
                break;
            case R.id.ff_play:
                startActivity(new Intent(this, FFPlayActivity.class));
                break;
            case R.id.media_codec_play_av:
                startActivity(new Intent(this, MediaCodecAVActivity.class));
                break;
            case R.id.media_codec_play_avsync:
                startActivity(new Intent(this, MediaCodecAVSyncActivity.class));
                break;
        }
    }
}