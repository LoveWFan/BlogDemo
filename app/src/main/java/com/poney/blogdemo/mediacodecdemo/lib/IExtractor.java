package com.poney.blogdemo.mediacodecdemo.lib;

import android.media.MediaFormat;

import java.nio.ByteBuffer;

/**
 * @author poney.ma
 * @project BlogDemo
 * @package_name com.poney.blogdemo.mediacodecdemo.lib
 * @date 2021/1/4
 */
public interface IExtractor {
    //获取音视频格式参数
    MediaFormat getFormat();

    //读取音视频数据
    int readBuffer(ByteBuffer byteBuffer);

    //获取当前帧时间
    long getCurrentTimeStamp();

    //Seek到指定位置，并返回实际帧的时间戳
    long seek(long pos);

    void setStartPos(long pos);

    //停止读取数据
    void stop();


}
