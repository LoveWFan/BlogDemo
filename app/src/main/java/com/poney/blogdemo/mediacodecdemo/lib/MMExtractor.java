package com.poney.blogdemo.mediacodecdemo.lib;

import android.media.MediaExtractor;
import android.media.MediaFormat;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author poney.ma
 * @project BlogDemo
 * @package_name com.poney.blogdemo.mediacodecdemo.lib
 * @date 2021/1/5
 */
public class MMExtractor {


    private MediaExtractor mExtractor = null;
    private int mAudioTrack = -1;
    private int mVideoTrack = -1;
    private long mCurSampleTime = 0;
    private long mStartPos = 0;

    public MMExtractor(String path) {
        try {
            mExtractor = new MediaExtractor();
            mExtractor.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取视频格式参数
     */
    public MediaFormat getVideoFormat() {
        for (int i = 0; i < mExtractor.getTrackCount(); i++) {
            MediaFormat trackFormat = mExtractor.getTrackFormat(i);
            if (trackFormat.getString(MediaFormat.KEY_MIME).startsWith("video/")) {
                mVideoTrack = i;
                break;
            }
        }

        return mVideoTrack >= 0 ? mExtractor.getTrackFormat(mVideoTrack) : null;
    }

    /**
     * 获取音频格式参数
     */
    public MediaFormat getAudioFormat() {
        for (int i = 0; i < mExtractor.getTrackCount(); i++) {
            MediaFormat trackFormat = mExtractor.getTrackFormat(i);
            if (trackFormat.getString(MediaFormat.KEY_MIME).startsWith("audio/")) {
                mAudioTrack = i;
                break;
            }
        }

        return mAudioTrack >= 0 ? mExtractor.getTrackFormat(mAudioTrack) : null;
    }

    /**
     * 读取视频数据
     */
    public int readBuffer(ByteBuffer byteBuffer) {
        //【3，提取数据】
        byteBuffer.clear();
        selectSourceTrack();
        int readSampleCount = mExtractor.readSampleData(byteBuffer, 0);
        if (readSampleCount < 0) {
            return -1;
        }
        mCurSampleTime = mExtractor.getSampleTime();

        //进入下一帧
        mExtractor.advance();
        return readSampleCount;
    }

    /**
     * 选择通道
     */
    private void selectSourceTrack() {
        if (mVideoTrack >= 0) {
            mExtractor.selectTrack(mVideoTrack);
        } else if (mAudioTrack >= 0) {
            mExtractor.selectTrack(mAudioTrack);
        }
    }

    public long seek(long pos) {
        mExtractor.seekTo(pos, MediaExtractor.SEEK_TO_PREVIOUS_SYNC);
        return mExtractor.getSampleTime();
    }

    public void stop() {
        mExtractor.release();
        mExtractor = null;
    }

    public int getAudioTrack() {
        return mAudioTrack;
    }

    public int getVideoTrack() {
        return mVideoTrack;
    }

    public long getCurSampleTime() {
        return mCurSampleTime;
    }

    public void setStartPos(long mStartPos) {
        this.mStartPos = mStartPos;
    }
}
