package com.poney.blogdemo.ffplaydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.poney.blogdemo.R;
import com.poney.ffmpeg.play.FFUtils;
import com.poney.ffmpeg.play.FFVideoView;

public class FFPlayActivity extends AppCompatActivity {
    private TextView mTextView;
    private FFVideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ff_play);

        mTextView = findViewById(R.id.sample_text);
        mVideoView = findViewById(R.id.videoView);
    }

    public void onButtonClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.button_protocol:
                setInfoText(FFUtils.urlProtocolInfo());
                break;
            case R.id.button_codec:
                setInfoText(FFUtils.avCodecInfo());
                break;
            case R.id.button_filter:
                setInfoText(FFUtils.avFilterInfo());
                break;
            case R.id.button_format:
                setInfoText(FFUtils.avFormatInfo());
                break;
            case R.id.button_play:
                String videoPath = Environment.getExternalStorageDirectory() + "/killer.mp4";

                mVideoView.playVideo(videoPath);
                break;
        }
    }

    private void setInfoText(String content) {
        if (mTextView != null) {
            mTextView.setText(content);
        }
    }
}