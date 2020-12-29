package com.poney.sdl2.hello;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import androidx.annotation.Nullable;

import com.poney.ffmpeg.base.AbsBaseActivity;
import com.poney.ffmpeg.task.AssertReleaseTask;

import org.libsdl.app.SDLActivity;


/**
 * @anchor: poney
 * @date: 2018-10-30
 * @description:
 */
public class SDLHelloActivity extends AbsBaseActivity implements AssertReleaseTask.ReleaseCallback {

    private TextView mBtnSDLShow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdl_hello);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mBtnSDLShow = (TextView) findViewById(R.id.btn_sdl_hello);
        AssertReleaseTask videoReleaseTask = new AssertReleaseTask(this, "input.bmp", this);
        videoReleaseTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void onSDLClick(View view) {
        Intent intent = new Intent(this, SDLActivity.class);
        startActivity(intent);
    }

    @Override
    public void onReleaseSuccess(String filePath) {
        mBtnSDLShow.setEnabled(true);
    }
}
