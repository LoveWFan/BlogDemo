package com.poney.ffmpeg.encoder;

import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.MediaRecorder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.poney.ffmpeg.audio.AudioRecordLoader;

import java.io.IOException;
import java.nio.ByteBuffer;

import static com.poney.ffmpeg.encoder.AACMediaCodecEncoder.AAC_ENCODER;
import static com.poney.ffmpeg.encoder.H264MediaCodecEncoder.H264_ENCODER;

public class Mp4MediaCodecRecord implements EncoderCallback {
    private final Object mLock;
    private AudioRecordLoader mAACMediaCodecEncoder;
    private H264MediaCodecEncoder mH264MediaCodecEncoder;
    private MediaMuxer mMediaMuxer;

    private volatile boolean mHasStartMuxer;
    private boolean mIsRecoding;
    private boolean mHasStopVideo;
    private boolean mHasStopAudio;
    private int mVideoTrackIndex = -1;
    private int mAudioTrackIndex = -1;

    public Mp4MediaCodecRecord(int width, int height, int sampleRateInHz, int channelConfig, String outputPath) {
        mH264MediaCodecEncoder = new H264MediaCodecEncoder(width, height);
        mAACMediaCodecEncoder = new AudioRecordLoader(MediaRecorder.AudioSource.MIC,
                sampleRateInHz,
                channelConfig,
                AACMediaCodecEncoder.DEFAULT_ENCODING,
                AudioRecord.getMinBufferSize(AACMediaCodecEncoder.DEFAULT_SAMPLE_RATE_IN_HZ, AACMediaCodecEncoder.DEFAULT_CHANNEL_CONFIG, AACMediaCodecEncoder.DEFAULT_ENCODING), null);
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
        try {
            synchronized (mLock) {
                if (mHasStartMuxer) {
                    return;
                }
                if (type == H264_ENCODER) {
                    mVideoTrackIndex = mMediaMuxer.addTrack(mediaFormat);
                }
                if (type == AAC_ENCODER) {
                    mAudioTrackIndex = mMediaMuxer.addTrack(mediaFormat);
                }
                if (mVideoTrackIndex == -1 ||
                        mAudioTrackIndex == -1) {
                    mLock.wait();
                } else {
                    mMediaMuxer.start();
                    mHasStartMuxer = true;
                    mLock.notifyAll();
                }

            }

        } catch (Exception e) {
            Log.e("MFB", "error:" + e.getLocalizedMessage());
        }
    }

    @Override
    public void onEncodeOutput(int type, ByteBuffer byteBuffer, MediaCodec.BufferInfo
            bufferInfo) {
        synchronized (mLock) {
            if (type == H264_ENCODER && mVideoTrackIndex != -1) {
                mMediaMuxer.writeSampleData(mVideoTrackIndex, byteBuffer, bufferInfo);
            }
            if (type == AAC_ENCODER && mAudioTrackIndex != -1) {
                mMediaMuxer.writeSampleData(mAudioTrackIndex, byteBuffer, bufferInfo);
            }

        }
    }


    @Override
    public void onStop(int type) {
        synchronized (mLock) {
            if (type == H264_ENCODER) {
                mHasStopVideo = true;
            }
            if (type == AAC_ENCODER) {
                mHasStopAudio = true;
            }
            if (mHasStopAudio && mHasStopVideo && mHasStartMuxer) {
                mHasStartMuxer = false;
                mMediaMuxer.stop();
            }
        }
    }

    public void start() {
        mIsRecoding = true;
        mAACMediaCodecEncoder.startRecord();
        mH264MediaCodecEncoder.startEncoder();
    }

    public void stop() {
        mAACMediaCodecEncoder.stopRecord();
        mH264MediaCodecEncoder.stopEncoder();
        mIsRecoding = false;
    }

    public void putVideoData(@Nullable byte[] data) {
        mH264MediaCodecEncoder.putData(data);
    }

}