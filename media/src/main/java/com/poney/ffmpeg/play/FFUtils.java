package com.poney.ffmpeg.play;

import android.view.Surface;

/**
 *
 */

public class FFUtils {
    static {
        System.loadLibrary("ffmpeg-lib");
    }

    public static native String urlProtocolInfo();

    public static native String avFormatInfo();

    public static native String avCodecInfo();

    public static native String avFilterInfo();

    public static native void playVideo(String videoPath, Surface surface);
}
