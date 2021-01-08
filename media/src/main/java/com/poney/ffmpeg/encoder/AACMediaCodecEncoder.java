package com.poney.ffmpeg.encoder;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AACMediaCodecEncoder {
    private static final int TIMEOUT_S = 10000;
    private BufferedOutputStream mBufferedOutputStream;
    private MediaCodec mMediaCodec;
    private volatile boolean isRunning = false;
    public final static int DEFAULT_SAMPLE_RATE_IN_HZ = 44_100;
    public final static int DEFAULT_CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    public final static int DEFAULT_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    public final static int DEFAULT_BUFFER_SIZE_IN_BYTES = 4096;


    private ArrayBlockingQueue<byte[]> pcmQueue = new ArrayBlockingQueue<>(10);
    private int mSampleRateInHz;


    public AACMediaCodecEncoder(int sampleRateInHz, int channelConfig, String outputPath) {
        mSampleRateInHz = sampleRateInHz;

        MediaFormat mediaFormat = MediaFormat.createAudioFormat(MediaFormat.MIMETYPE_AUDIO_AAC, sampleRateInHz, channelConfig == AudioFormat.CHANNEL_OUT_MONO ? 1 : 2);
        //声音中的比特率是指将模拟声音信号转换成数字声音信号后，单位时间内的二进制数据量，是间接衡量音频质量的一个指标
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 96000);//64000, 96000, 128000
        mediaFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, AudioRecord.getMinBufferSize(DEFAULT_SAMPLE_RATE_IN_HZ, DEFAULT_CHANNEL_CONFIG, DEFAULT_ENCODING));
        mediaFormat.setInteger(MediaFormat.KEY_CHANNEL_COUNT, channelConfig == AudioFormat.CHANNEL_OUT_MONO ? 1 : 2);

        try {
            mMediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_AAC);
            mMediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mMediaCodec.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(outputPath)) {
            return;
        }
        File file = new File(outputPath);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            mBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file), DEFAULT_BUFFER_SIZE_IN_BYTES);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void putData(byte[] buffer) {
        if (pcmQueue.size() >= 10) {
            pcmQueue.poll();
        }
        pcmQueue.add(buffer);
    }

    public void startEncoder() {
        isRunning = true;
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                byte[] input = null;
                byte[] aacChunk;
                while (isRunning) {
                    if (pcmQueue.size() > 0) {
                        input = pcmQueue.poll();
                    }
                    if (input != null) {
                        try {
                            int inputBufferIndex = mMediaCodec.dequeueInputBuffer(TIMEOUT_S);
                            if (inputBufferIndex >= 0) {

                                ByteBuffer inputBuffer = null;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                    inputBuffer = mMediaCodec.getInputBuffer(inputBufferIndex);
                                } else {
                                    inputBuffer = mMediaCodec.getInputBuffers()[inputBufferIndex];
                                }
                                inputBuffer.clear();
                                inputBuffer.limit(input.length);
                                inputBuffer.put(input);
                                mMediaCodec.queueInputBuffer(inputBufferIndex, 0, input.length, 0, 0);
                            }

                            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                            int outputBufferIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, TIMEOUT_S);
                            while (outputBufferIndex >= 0) {
                                ByteBuffer outputBuffer = null;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                    outputBuffer = mMediaCodec.getOutputBuffer(outputBufferIndex);
                                } else {
                                    outputBuffer = mMediaCodec.getOutputBuffers()[outputBufferIndex];
                                }
                                if (bufferInfo.flags == MediaCodec.BUFFER_FLAG_CODEC_CONFIG) {
                                    bufferInfo.size = 0;
                                }

                                if (bufferInfo.size > 0) {
                                    // adjust the ByteBuffer values to match BufferInfo (not needed?)
                                    outputBuffer.position(bufferInfo.offset);
                                    outputBuffer.limit(bufferInfo.offset + bufferInfo.size);

                                    aacChunk = new byte[bufferInfo.size + 7];
                                    addADTStoPacket(mSampleRateInHz, aacChunk, aacChunk.length);
                                    outputBuffer.get(aacChunk, 7, bufferInfo.size);
                                    try {
                                        Log.i("MFB", "write aacChunk:" + aacChunk.length);
                                        mBufferedOutputStream.write(aacChunk);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                mMediaCodec.releaseOutputBuffer(outputBufferIndex, false);
                                bufferInfo = new MediaCodec.BufferInfo();
                                outputBufferIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, TIMEOUT_S);
                            }
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }


                    } else {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    Log.i("MFB", "flush");
                    mBufferedOutputStream.flush();
                    mBufferedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addADTStoPacket(int sampleRateInHz, byte[] packet, int packetLen) {
        int profile = 2;
        int chanCfg = 2;
        packet[0] = (byte) 0xFF;
        packet[1] = (byte) 0xF9;
        packet[2] = (byte) (((profile - 1) << 6) + (sampleRateInHz << 2) + (chanCfg >> 2));
        packet[3] = (byte) (((chanCfg & 3) << 6) + (packetLen >> 11));
        packet[4] = (byte) ((packetLen & 0x7FF) >> 3);
        packet[5] = (byte) (((packetLen & 7) << 5) + 0x1F);
        packet[6] = (byte) 0xFC;
    }

    private long getPts() {
        return System.nanoTime() / 1000L;
    }

    public void stopEncoder() {
        isRunning = false;
        try {
            mMediaCodec.stop();
            mMediaCodec.release();
            mMediaCodec = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}