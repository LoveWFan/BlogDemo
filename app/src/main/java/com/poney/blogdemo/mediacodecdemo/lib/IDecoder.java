package com.poney.blogdemo.mediacodecdemo.lib;

/**
 * @author poney.ma
 * @project BlogDemo
 * @package_name com.poney.blogdemo.mediacodecdemo.lib
 * @date 2021/1/4
 */
public interface IDecoder extends Runnable {
    //继续解码
    void resume();

    //暂停解码
    void pause();

    //停止解码
    void stop();

    //是否正在解码
    boolean isDecoding();

    //是否正在快进
    boolean isSeeking();

    //是否停止解码
    boolean isStop();

    void setStateListener(IStateListener stateListener);
}
