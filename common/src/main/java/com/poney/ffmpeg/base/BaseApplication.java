package com.poney.ffmpeg.base;

import android.app.Application;

import com.poney.ffmpeg.core.AppCore;

/**
 * @anchor: poney
 * @date: 2018-11-07
 * @description:
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppCore.getInstance().init(this);
    }
}
