package com.poney.blogdemo.mediacodecdemo.lib;

/**
 * @author poney.ma
 * @project BlogDemo
 * @package_name com.poney.blogdemo.mediacodecdemo.lib
 * @date 2021/1/4
 */
public interface IStateListener {
    void decoderPrepare(BaseDecoder baseDecoder);
    void decoderReady(BaseDecoder baseDecoder);
    void decoderRunning(BaseDecoder baseDecoder);
    void decoderPause(BaseDecoder baseDecoder);
    void decoderFinish(BaseDecoder baseDecoder);
    void decoderDestroy(BaseDecoder baseDecoder);
    void decoderError(BaseDecoder baseDecoder, String reason);
}
