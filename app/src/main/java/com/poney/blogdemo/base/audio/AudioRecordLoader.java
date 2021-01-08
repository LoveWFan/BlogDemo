package com.poney.blogdemo.base.audio;

import android.media.AudioRecord;
import android.util.Log;

import com.poney.ffmpeg.encoder.AACMediaCodecEncoder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.poney.ffmpeg.encoder.AACMediaCodecEncoder.DEFAULT_BUFFER_SIZE_IN_BYTES;

/**
 * @author feibiao.ma
 * @project BlogDemo
 * @package_name com.poney.blogdemo.base.audio
 * @date 2021/1/8
 */
public class AudioRecordLoader {
    private boolean mIsRecording;
    private AudioRecord mAudioRecord;
    private AACMediaCodecEncoder mAACMediaCodecEncoder;


    public AudioRecordLoader(int audioSource, int sampleRateInHz, int channelConfig, int audioFormat, int bufferSizeInBytes, String outPath) {
        mAudioRecord = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes);
        mAACMediaCodecEncoder = new AACMediaCodecEncoder(sampleRateInHz, channelConfig);
        mAACMediaCodecEncoder.setOutputPath(outPath);
    }


    public void startRecord() {
        mAudioRecord.startRecording();
        mAACMediaCodecEncoder.startEncoder();
        mIsRecording = true;
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                byte[] buffer = new byte[DEFAULT_BUFFER_SIZE_IN_BYTES];
                while (mIsRecording) {
                    int len = mAudioRecord.read(buffer, 0, DEFAULT_BUFFER_SIZE_IN_BYTES);
                    if (len > 0) {
                        byte[] data = new byte[len];
                        System.arraycopy(buffer, 0, data, 0, len);
                        mAACMediaCodecEncoder.putData(data);
                    }
                }
            }
        });

    }

    public void stopRecord() {
        mIsRecording = false;
        mAACMediaCodecEncoder.stopEncoder();
        mAudioRecord.stop();
    }

    public void release() {
        mAudioRecord.release();
        mAACMediaCodecEncoder = null;
        mAudioRecord = null;
    }
}
