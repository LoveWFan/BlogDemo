package com.poney.ffmpeg.encoder;


public class H264FFMPEGEncoder {

    static {
        System.loadLibrary("ffmpeg-lib");
    }

    public native void encodeMP4Start(String mp4Path, int width, int height);

    public native void encodeMP4Stop();

    public native void onPreviewFrame(byte[] yuvData, int width, int height);

    public native void encodeJPEG(String jpegPath, int width, int height);

}
