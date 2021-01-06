package com.poney.blogdemo.mediacodecdemo.lib;

import android.media.MediaFormat;

import java.nio.ByteBuffer;

/**
 * @author poney.ma
 * @project BlogDemo
 * @package_name com.poney.blogdemo.mediacodecdemo.lib
 * @date 2021/1/5
 */
public class AudioExtractor implements IExtractor {
    private MMExtractor mmExtractor;

    public AudioExtractor(String path) {
        this.mmExtractor = new MMExtractor(path);
    }


    @Override
    public MediaFormat getFormat() {
        return mmExtractor.getAudioFormat();
    }

    @Override
    public int readBuffer(ByteBuffer byteBuffer) {
        return mmExtractor.readBuffer(byteBuffer);
    }

    @Override
    public long getCurrentTimeStamp() {
        return mmExtractor.getCurSampleTime();
    }

    @Override
    public long seek(long pos) {
        return mmExtractor.seek(pos);
    }

    @Override
    public void setStartPos(long pos) {
        mmExtractor.setStartPos(pos);
    }

    @Override
    public void stop() {
        mmExtractor.stop();
    }
}
