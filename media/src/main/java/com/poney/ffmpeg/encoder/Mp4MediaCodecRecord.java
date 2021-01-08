package com.poney.ffmpeg.encoder;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;

import static com.poney.ffmpeg.encoder.AACMediaCodecEncoder.AAC_ENCODER;
import static com.poney.ffmpeg.encoder.H264MediaCodecEncoder.H264_ENCODER;

public class Mp4MediaCodecRecord implements EncoderCallback {
    private final Object mLock;
    private AACMediaCodecEncoder mAACMediaCodecEncoder;
    private H264MediaCodecEncoder mH264MediaCodecEncoder;
    private MediaMuxer mMediaMuxer;

    private boolean mHasStartMuxer;
    private boolean mIsRecoding;
    private BlockingQueue<AVData> mDataBlockingQueue;

    public Mp4MediaCodecRecord(int width, int height, int sampleRateInHz, int channelConfig, String outputPath) {
        mH264MediaCodecEncoder = new H264MediaCodecEncoder(width, height);
        mAACMediaCodecEncoder = new AACMediaCodecEncoder(sampleRateInHz, channelConfig);
        mH264MediaCodecEncoder.setEncoderCallback(this);
        mAACMediaCodecEncoder.setEncoderCallback(this);
        try {
            mMediaMuxer = new MediaMuxer(outputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mHasStartMuxer = false;
        mLock = new Object();
    }

    @Override
    public void outputMediaFormatChanged(int type, MediaFormat mediaFormat) {
        Log.i("MFB", "outputMediaFormatChanged:" + type);
    }

    @Override
    public void onEncodeOutput(int type, ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) {
        Log.i("MFB", "writeMediaData:" + type);
        writeMediaData(type, byteBuffer, bufferInfo);
    }

    private void checkMediaFormat(int type, MediaFormat mediaFormat) {

    }

    private void writeMediaData(int type, ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) {

        mDataBlockingQueue.add(new AVData(type, byteBuffer, bufferInfo));
    }

    @Override
    public void onStop(int type) {

    }

    public void start() {
        mIsRecoding = true;
        mAACMediaCodecEncoder.startEncoder();
        mH264MediaCodecEncoder.startEncoder();
    }

    public void stop() {
        mAACMediaCodecEncoder.stopEncoder();
        mH264MediaCodecEncoder.stopEncoder();
        mIsRecoding = false;
    }

    private class AVData {
        int type;
        ByteBuffer byteBuffer;
        MediaCodec.BufferInfo bufferInfo;

        public AVData(int type, ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) {
            this.type = type;
            this.byteBuffer = byteBuffer;
            this.bufferInfo = bufferInfo;
        }
    }
}