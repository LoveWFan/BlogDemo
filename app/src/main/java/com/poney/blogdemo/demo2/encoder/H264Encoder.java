package com.poney.blogdemo.demo2.encoder;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jiazhu
 */
public class H264Encoder {
    private static final int TIMEOUT_S = 10000;
    private MediaCodec mMediaCodec;
    private boolean isRunning = false;


    private ArrayBlockingQueue<byte[]> yuv420Queue = new ArrayBlockingQueue<>(10);
    private MediaMuxer mMuxer;
    private int mTrackIndex;
    private boolean mMuxerStarted;


    public H264Encoder(int width, int height, int frameRate, String outputPath) {
        MediaFormat mediaFormat = MediaFormat.createVideoFormat("video/avc", width, height);
        mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible);
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, width * height * 5);
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, frameRate);
        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);

        try {
            mMediaCodec = MediaCodec.createEncoderByType("video/avc");
            mMediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mMediaCodec.start();


            // Create a MediaMuxer.  We can't add the video track and start() the muxer here,
            // because our MediaFormat doesn't have the Magic Goodies.  These can only be
            // obtained from the encoder after it has started processing data.
            //
            // We're not actually interested in multiplexing audio.  We just want to convert
            // the raw H.264 elementary stream we get from MediaCodec into a .mp4 file.
            try {
                mMuxer = new MediaMuxer(outputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            } catch (IOException ioe) {
                throw new RuntimeException("MediaMuxer creation failed", ioe);
            }

            mTrackIndex = -1;
            mMuxerStarted = false;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void putData(byte[] buffer) {
        if (yuv420Queue.size() >= 10) {
            yuv420Queue.poll();
        }
        yuv420Queue.add(buffer);
    }

    public void startEncoder() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                isRunning = true;
                byte[] input = null;
                while (isRunning) {
                    if (yuv420Queue.size() > 0) {
                        input = yuv420Queue.poll();
                    }
                    if (input != null) {
                        try {
                            int inputBufferIndex = mMediaCodec.dequeueInputBuffer(TIMEOUT_S);
                            if (inputBufferIndex >= 0) {
                                long pts = getPts();
                                ByteBuffer inputBuffer = mMediaCodec.getInputBuffer(inputBufferIndex);
                                inputBuffer.clear();
                                inputBuffer.put(input);
                                mMediaCodec.queueInputBuffer(inputBufferIndex, 0, input.length, pts, 0);
                            }

                            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                            int outputBufferIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, TIMEOUT_S);
                            if (outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                                if (mMuxerStarted) {
                                    throw new RuntimeException("format changed twice");
                                }
                                MediaFormat newFormat = mMediaCodec.getOutputFormat();

                                // now that we have the Magic Goodies, start the muxer
                                mTrackIndex = mMuxer.addTrack(newFormat);
                                mMuxer.start();
                                mMuxerStarted = true;
                            }

                            while (outputBufferIndex >= 0) {
                                ByteBuffer outputBuffer = mMediaCodec.getOutputBuffer(outputBufferIndex);
                                if (bufferInfo.flags == MediaCodec.BUFFER_FLAG_CODEC_CONFIG) {
                                    bufferInfo.size = 0;
                                }

                                if (bufferInfo.size > 0) {
                                    if (!mMuxerStarted) {
                                        throw new RuntimeException("muxer hasn't started");
                                    }
                                    // adjust the ByteBuffer values to match BufferInfo (not needed?)
                                    outputBuffer.position(bufferInfo.offset);
                                    outputBuffer.limit(bufferInfo.offset + bufferInfo.size);

                                    mMuxer.writeSampleData(mTrackIndex, outputBuffer, bufferInfo);
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
                    mMediaCodec.stop();
                    mMediaCodec.release();
                    mMediaCodec = null;
                    if (mMuxer != null) {
                        mMuxer.stop();
                        mMuxer.release();
                        mMuxer = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private long getPts() {
        return System.nanoTime() / 1000L;
    }

    public void stopEncoder() {
        isRunning = false;
    }


}