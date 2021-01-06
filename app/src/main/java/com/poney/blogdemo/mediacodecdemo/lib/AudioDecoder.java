package com.poney.blogdemo.mediacodecdemo.lib;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;

import java.nio.ByteBuffer;

/**
 * @author poney.ma
 * @project BlogDemo
 * @package_name com.poney.blogdemo.mediacodecdemo.lib
 * @date 2021/1/5
 */
public class AudioDecoder extends BaseDecoder {
    private final String TAG = "AudioDecoder";
    //采样率
    private int mSampleRate = -1;
    //声道数
    private int mChannels = -1;
    //采样位数
    private int mPcmEncodeBit = AudioFormat.ENCODING_PCM_16BIT;

    //音频播放器
    private AudioTrack mAudioTrack = null;
    private short[] mAudioOutTempBuf = null;


    //音频数据缓存
    public AudioDecoder(String filePath) {
        super(filePath, DecodeType.AUDIO);
    }

    @Override
    protected void doneDecode() {
        mAudioTrack.stop();
        mAudioTrack.release();
    }

    @Override
    protected void render(ByteBuffer outputBuffer, MediaCodec.BufferInfo bufferInfo) {
        if (mAudioOutTempBuf == null || mAudioOutTempBuf.length < bufferInfo.size / 2) {
            mAudioOutTempBuf = new short[bufferInfo.size / 2];
        }
        outputBuffer.position(0);
        outputBuffer.asShortBuffer().get(mAudioOutTempBuf, 0, bufferInfo.size / 2);
        mAudioTrack.write(mAudioOutTempBuf, 0, bufferInfo.size / 2);
    }

    @Override
    protected boolean configCodec(MediaCodec codec, MediaFormat format) {
        codec.configure(format, null, null, 0);
        return true;
    }

    @Override
    protected boolean initRender() {
        int channel = mChannels == 1 ? AudioFormat.CHANNEL_OUT_MONO : AudioFormat.CHANNEL_OUT_STEREO;

        int minBufferSize = AudioTrack.getMinBufferSize(mSampleRate, channel, mPcmEncodeBit);
        mAudioOutTempBuf = new short[minBufferSize / 2];
        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                mSampleRate,
                channel,
                mPcmEncodeBit,
                minBufferSize,
                AudioTrack.MODE_STREAM);
        mAudioTrack.play();
        return true;
    }

    @Override
    protected void initSpecParams(MediaFormat format) {
        try {
            mChannels = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
            mSampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);

            mPcmEncodeBit = format.containsKey(MediaFormat.KEY_PCM_ENCODING) ? format.getInteger(MediaFormat.KEY_PCM_ENCODING) : AudioFormat.ENCODING_PCM_16BIT;
        } catch (Exception e) {
        }
    }

    @Override
    protected IExtractor initExtractor(String filePath) {
        return new AudioExtractor(filePath);
    }

    @Override
    protected boolean check() {
        return true;
    }
}
