package com.poney.blogdemo.mediacodecdemo.play;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.MediaController;

import androidx.appcompat.app.AppCompatActivity;

import com.poney.blogdemo.R;
import com.poney.blogdemo.mediacodecdemo.lib.AudioDecoder;
import com.poney.blogdemo.mediacodecdemo.lib.VideoDecoder;
import com.poney.blogdemo.mediacodecdemo.play.advance.MediaCodecPlayer;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * MediaCodec 音视频同步播放 升级版
 */
public class MediaCodecAVSyncActivity extends AppCompatActivity implements SurfaceHolder.Callback2 {

    @BindView(R.id.sfv)
    SurfaceView sfv;
    @BindView(R.id.pause_resume)
    Button pauseResume;
    private static final String TAG = "MediaCodecAVSync";
    private MediaCodecPlayer mMediaCodecPlayer;
    private SurfaceHolder mSurfaceHolder;
    private static final int SLEEP_TIME_MS = 1000;
    private static final long PLAY_TIME_MS = TimeUnit.MILLISECONDS.convert(4, TimeUnit.MINUTES);
    //    public static final String FILE_URI = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mvtest.mp4";
    public static final String FILE_URI = "https://raw.githubusercontent.com/LoveWFan/BlogDemo/master/source/mvtest.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_codec_a_v);
        ButterKnife.bind(this);
        sfv.getHolder().addCallback(this);

        startMusicCommand();
    }

    private void startMusicCommand() {
        Intent intent = new Intent("com.android.music.musicservicecommand");
        intent.putExtra("command", "pause");
        this.sendBroadcast(intent);
    }


    @Override
    public void surfaceRedrawNeeded(SurfaceHolder holder) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceHolder = holder;
        mSurfaceHolder.setKeepScreenOn(true);
        new DecodeTask().execute();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopPlayer();
    }

    private void stopPlayer() {
        if (mMediaCodecPlayer != null) {
            mMediaCodecPlayer.reset();
        }

    }


    public class DecodeTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            //this runs on a new thread
            initializePlayer();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            //this runs on ui thread
        }
    }

    private void initializePlayer() {
        mMediaCodecPlayer = new MediaCodecPlayer(mSurfaceHolder, getApplicationContext());

        mMediaCodecPlayer.setAudioDataSource(Uri.parse(FILE_URI), null);
        mMediaCodecPlayer.setVideoDataSource(Uri.parse(FILE_URI), null);
        mMediaCodecPlayer.start(); //from IDLE to PREPARING
        try {
            mMediaCodecPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // starts video playback
        mMediaCodecPlayer.startThread();

        long timeOut = System.currentTimeMillis() + 4 * PLAY_TIME_MS;
        while (timeOut > System.currentTimeMillis() && !mMediaCodecPlayer.isEnded()) {
            try {
                Thread.sleep(SLEEP_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (mMediaCodecPlayer.getCurrentPosition() >= mMediaCodecPlayer.getDuration()) {
                Log.d(TAG, "testVideoPlayback -- current pos = " +
                        mMediaCodecPlayer.getCurrentPosition() +
                        ">= duration = " + mMediaCodecPlayer.getDuration());
                break;
            }
        }

        if (timeOut > System.currentTimeMillis()) {
            Log.e(TAG, "video playback timeout exceeded!");
            return;
        }

        Log.d(TAG, "playVideo player.reset()");
        mMediaCodecPlayer.reset();
    }
}