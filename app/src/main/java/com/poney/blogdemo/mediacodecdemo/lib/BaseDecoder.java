package com.poney.blogdemo.mediacodecdemo.lib;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;

import java.io.File;
import java.nio.ByteBuffer;

/**
 * @author poney.ma
 * @project BlogDemo
 * @package_name com.poney.blogdemo.mediacodecdemo.lib
 * @date 2021/1/4
 */
public abstract class BaseDecoder implements IDecoder {
    private final String TAG = "BaseDecoder";

    //--------------线程相关---------
    //解码器是否正在运行
    private boolean mIsRunning = true;
    //线程等待锁
    private Object mLock = new Object();


    //--------------解码相关---------
    //音视频解码器
    protected MediaCodec mCodec = null;
    //音视频数据读取器
    protected IExtractor mExtractor = null;
    //解码输入缓冲区
    protected ByteBuffer[] mInputBuffers = null;
    //解码输出缓冲区
    protected ByteBuffer[] mOutputBuffers = null;
    //解码数据信息
    private MediaCodec.BufferInfo mBufferInfo = new MediaCodec.BufferInfo();
    //解码状态回调函数
    protected IStateListener mStateListener;
    //解码状态
    private volatile DecodeState mState = DecodeState.STOP;
    //解码类型
    private volatile DecodeType mDecodeType = DecodeType.UNKNOWN;
    //流数据是否结束
    private boolean mIsEOS = false;
    private int mVideoWidth = 0;
    private int mVideoHeight = 0;

    //解码文件路径
    private String mFilePath;
    private long mDuration;
    private double mEndPos;

    //音视频同步---音频和视频都同步到系统时钟
    private long mStartTimeForSync = -1L;

    //音视频同步---视频同步到音频
    private long mVideoClock = -1L;
    private long mAudioClock = -1L;

    public BaseDecoder(String filePath, DecodeType decodeType) {
        this.mFilePath = filePath;
        this.mDecodeType = decodeType;
    }

    @Override
    public void run() {
        if (mState == DecodeState.STOP) {
            mState = DecodeState.START;
        }
        if (mStateListener != null)
            mStateListener.decoderPrepare(this);
        //【解码步骤:1.初始化，并启动解码器】
        if (!init())
            return;
        Log.i(TAG, "开始解码");
        try {
            while (mIsRunning) {
                if (mState != DecodeState.START
                        && mState != DecodeState.DECODING
                        && mState != DecodeState.SEEKING) {
                    Log.i(TAG, "进入等待：" + mState);
                    waitDecode();

                    //----------【根据系统时间 同步时间矫正】------------
                    //恢复同步的起始时间,即去除等待流失的时间
                    mStartTimeForSync = System.currentTimeMillis() - getCurTimeStamp();
                }
                if (!mIsRunning || mState == DecodeState.STOP) {
                    mIsRunning = false;
                    break;
                }
                //------------【根据系统时间 音视频同步】-----------
                if (mStartTimeForSync == -1L) {
                    mStartTimeForSync = System.currentTimeMillis();
                }
                //如果数据没有解码完毕，将数据推入解码器
                if (!mIsEOS) {
                    //【解码步骤:2.将数据压入解码器缓冲】
                    mIsEOS = pushBufferToDecoder();
                }
                //【解码步骤:3.将解码好的数据从缓冲区拉取出来】
                int index = pullBufferFromDecoder();
                if (index > 0) {
                    //-------------【视频同步到系统时钟】获取音视频PTS----------
                    if (mState == DecodeState.DECODING) {
                        sleepRender();
                    }
                    //【解码步骤:4.渲染】
                    //-------------【视频同步到音频】获取音视频PTS----------
                    if (mDecodeType == DecodeType.VIDEO) {
                        mVideoClock = getCurTimeStamp();
                    } else if (mDecodeType == DecodeType.AUDIO) {
                        mAudioClock = getCurTimeStamp();
                        render(mOutputBuffers[index], mBufferInfo);
                    }

                    //【解码步骤:5.释放输出缓冲】
                    mCodec.releaseOutputBuffer(index, true);

                    if (mState == DecodeState.START) {
                        mState = DecodeState.PAUSE;
                    }
                }

                //【解码步骤:6.判断解码是否完成】
                if (mBufferInfo.flags == MediaCodec.BUFFER_FLAG_END_OF_STREAM) {
                    mState = DecodeState.FINISH;
                    if (mStateListener != null)
                        mStateListener.decoderFinish(this);
                }
            }
        } catch (Exception e) {

        } finally {
            doneDecode();
            //【解码步骤:7.释放解码器】
            release();
        }

    }


    /**
     * 【根据系统时间同步音视频】
     */
    private void sleepRender() {
        long passTime = System.currentTimeMillis() - mStartTimeForSync;
        long curTimeStamp = getCurTimeStamp();
        if (curTimeStamp > passTime) {
            try {
                Thread.sleep(curTimeStamp - passTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private long getCurTimeStamp() {
        return mBufferInfo.presentationTimeUs / 1000;
    }

    /**
     * 释放解码器
     */
    public void release() {
        try {
            mState = DecodeState.STOP;
            mIsEOS = false;
            mExtractor.stop();
            mCodec.stop();
            mCodec.release();
            mStateListener.decoderDestroy(this);
        } catch (Exception e) {
        }
    }

    /**
     * 结束解码
     */
    protected abstract void doneDecode();

    /**
     * 渲染
     *
     * @param byteBuffer
     * @param mBufferInfo
     */
    protected abstract void render(ByteBuffer byteBuffer, MediaCodec.BufferInfo mBufferInfo);

    /**
     * 从解码器拉取缓冲
     *
     * @return
     */
    private int pullBufferFromDecoder() {
        // 查询是否有解码完成的数据，index >=0 时，表示数据有效，并且index为缓冲区索引
        int index = mCodec.dequeueOutputBuffer(mBufferInfo, 1000);
        switch (index) {
            case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
            case MediaCodec.INFO_TRY_AGAIN_LATER:
                break;
            case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
                mOutputBuffers = mCodec.getOutputBuffers();
                break;
            default:
                return index;
        }
        return -1;
    }

    /**
     * 将数据压入解码器缓冲
     *
     * @return
     */
    private boolean pushBufferToDecoder() {
        int inputBufferIndex = mCodec.dequeueInputBuffer(1000);
        boolean isEndOfStream = false;
        if (inputBufferIndex >= 0) {
            ByteBuffer inputBuffer = mInputBuffers[inputBufferIndex];
            int sampleSize = mExtractor.readBuffer(inputBuffer);
            if (sampleSize < 0) {
                //如果数据已经取完,压入数据结束标志:BUFFER_FLAG_END_OF_STREAM
                mCodec.queueInputBuffer(inputBufferIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                isEndOfStream = true;
            } else {
                mCodec.queueInputBuffer(inputBufferIndex, 0, sampleSize, mExtractor.getCurrentTimeStamp(), 0);
            }
        }
        return isEndOfStream;
    }

    /**
     * 等待解码
     */
    private void waitDecode() {
        try {
            if (mState == DecodeState.PAUSE) {
                if (mStateListener != null)
                    mStateListener.decoderPause(this);
            }
            synchronized (mLock) {
                mLock.wait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化解码器
     *
     * @return
     */
    private boolean init() {

        //1.检查参数是否完整
        if (mFilePath.isEmpty() || (!mFilePath.startsWith("http") && !new File(mFilePath).exists())) {
            if (mStateListener != null)
                mStateListener.decoderError(this, "文件路径为空");
            Log.w(TAG, "文件路径为空");
            return false;
        }
        //调用虚函数，检查子类参数是否完整
        if (!check()) {
            Log.w(TAG, "check() 失败");
            return false;
        }
        //2.初始化数据提取器
        mExtractor = initExtractor(mFilePath);
        if (mExtractor == null ||
                mExtractor.getFormat() == null) {
            Log.w(TAG, mExtractor == null ? "mExtractor 为空" : "mExtractor.getFormat() 为空");
            return false;
        }
        //3.初始化参数
        if (!initParams()) {
            Log.w(TAG, "initParams() 失败");
            return false;
        }
        //4.初始化渲染器
        if (!initRender()) {
            Log.w(TAG, "initRender() 失败");
            return false;
        }
        //5.初始化解码器
        if (!initCodec()) {
            Log.w(TAG, "initCodec() 失败");
            return false;
        }
        return true;
    }

    /**
     * 初始化解码器
     */
    private boolean initCodec() {
        try {
            //1.根据音视频编码格式初始化解码器
            String type = mExtractor.getFormat().getString(MediaFormat.KEY_MIME);
            mCodec = MediaCodec.createDecoderByType(type);
            //2.配置解码器
            if (!configCodec(mCodec, mExtractor.getFormat())) {
                waitDecode();
            }
            //3.启动解码器
            mCodec.start();

            //4.获取解码器缓冲区
            mInputBuffers = mCodec.getInputBuffers();
            mOutputBuffers = mCodec.getOutputBuffers();
        } catch (Exception e) {

        }
        return true;
    }

    /**
     * 配置解码器
     *
     * @param mCodec
     * @param format
     * @return
     */
    protected abstract boolean configCodec(MediaCodec mCodec, MediaFormat format);

    /**
     * 初始化渲染器
     */
    protected abstract boolean initRender();


    private boolean initParams() {
        try {
            MediaFormat format = mExtractor.getFormat();
            mDuration = format.getLong(MediaFormat.KEY_DURATION) / 1000;
            if (mEndPos == 0L) mEndPos = mDuration;

            initSpecParams(mExtractor.getFormat());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 配置子类特有参数
     */
    protected abstract void initSpecParams(MediaFormat format);

    /**
     * 初始化数据提取器
     */
    protected abstract IExtractor initExtractor(String filePath);

    /**
     * 检查子类参数
     */
    protected abstract boolean check();

    @Override
    public void resume() {
        Log.i(TAG, "resume");
        mState = DecodeState.DECODING;
        notifyDecode();
    }

    protected void notifyDecode() {
        synchronized (mLock) {
            mLock.notifyAll();
        }
        if (mState == DecodeState.DECODING) {
            if (mStateListener != null)
                mStateListener.decoderRunning(this);
        }
    }

    @Override
    public void pause() {
        mState = DecodeState.PAUSE;
    }

    @Override
    public void stop() {
        mIsRunning = false;
        mState = DecodeState.STOP;
        notifyDecode();
    }

    @Override
    public boolean isDecoding() {
        return mState == DecodeState.DECODING;
    }

    @Override
    public boolean isSeeking() {
        return mState == DecodeState.SEEKING;
    }

    @Override
    public boolean isStop() {
        return mState == DecodeState.STOP;
    }

    @Override
    public void setStateListener(IStateListener stateListener) {
        mStateListener = stateListener;
    }
}
