package com.poney.ffmpeg.encoder;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *MediaCodec同步模式
 */
public class H264MediaCodecEncoder extends MediaEncoder {
    public static final String MIMETYPE_VIDEO_AVC = "video/avc";
    public static final int H264_ENCODER = 1;
    private static final int TIMEOUT_S = 10000;
    private MediaCodec mMediaCodec;
    private volatile boolean isRunning = false;


    private ArrayBlockingQueue<byte[]> yuv420Queue = new ArrayBlockingQueue<>(10);
    private MediaMuxer mMuxer;
    private int mTrackIndex;
    private boolean mMuxerStarted;
    private volatile EncoderCallback mEncoderCallback;

    public void setEncoderCallback(EncoderCallback encoderCallback) {
        mEncoderCallback = encoderCallback;
    }


    public H264MediaCodecEncoder(int width, int height) {
        MediaFormat mediaFormat = MediaFormat.createVideoFormat(MIMETYPE_VIDEO_AVC, width, height);
        mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible);
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, width * height * 5);
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 30);//FPS
        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);

        try {
            mMediaCodec = MediaCodec.createEncoderByType(MIMETYPE_VIDEO_AVC);
            mMediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mMediaCodec.start();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setOutputPath(String outputPath) {
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
    }

    public void putData(byte[] buffer) {
        if (yuv420Queue.size() >= 10) {
            yuv420Queue.poll();
        }
        yuv420Queue.add(buffer);
    }

    public void startEncoder() {
        isRunning = true;
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                byte[] input = null;
                while (isRunning) {
                    if (yuv420Queue.size() > 0) {
                        input = yuv420Queue.poll();
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
                                inputBuffer.put(input);
                                mMediaCodec.queueInputBuffer(inputBufferIndex, 0, input.length, getPTSUs(), 0);
                            }

                            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                            int outputBufferIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, TIMEOUT_S);
                            if (outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                                MediaFormat newFormat = mMediaCodec.getOutputFormat();
                                if (null != mEncoderCallback) {
                                    mEncoderCallback.outputMediaFormatChanged(H264_ENCODER, newFormat);
                                }
                                if (mMuxer != null) {
                                    if (mMuxerStarted) {
                                        throw new RuntimeException("format changed twice");
                                    }
                                    // now that we have the Magic Goodies, start the muxer
                                    mTrackIndex = mMuxer.addTrack(newFormat);
                                    mMuxer.start();

                                    mMuxerStarted = true;
                                }
                            }

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
                                    // write encoded data to muxer(need to adjust presentationTimeUs.
                                    bufferInfo.presentationTimeUs = getPTSUs();

                                    if (mEncoderCallback != null) {
                                        //回调
                                        mEncoderCallback.onEncodeOutput(H264_ENCODER, outputBuffer, bufferInfo);
                                    }
                                    prevOutputPTSUs = bufferInfo.presentationTimeUs;
                                    if (mMuxer != null) {
                                        if (!mMuxerStarted) {
                                            throw new RuntimeException("muxer hasn't started");
                                        }
                                        mMuxer.writeSampleData(mTrackIndex, outputBuffer, bufferInfo);
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

            }
        });
    }


    public void stopEncoder() {
        if (mEncoderCallback != null) {
            //回调
            mEncoderCallback.onStop(H264_ENCODER);
        }
        isRunning = false;
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


}