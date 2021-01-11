package com.poney.ffmpeg.encoder;

/**
 * @author feibiao.ma
 * @project BlogDemo
 * @package_name com.poney.ffmpeg.encoder
 * @date 2021/1/11
 */
public abstract class MediaEncoder {
    /**
     * previous presentationTimeUs for writing
     */
    protected long prevOutputPTSUs = 0;

    /**
     * get next encoding presentationTimeUs
     *
     * @return
     */
    protected long getPTSUs() {
        long result = System.nanoTime() / 1000L;
        // presentationTimeUs should be monotonic
        // otherwise muxer fail to write
        if (result < prevOutputPTSUs)
            result = (prevOutputPTSUs - result) + result;
        return result;
    }
}
