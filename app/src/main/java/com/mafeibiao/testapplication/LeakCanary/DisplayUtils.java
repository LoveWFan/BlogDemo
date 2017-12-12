package com.mafeibiao.testapplication.LeakCanary;

import android.content.Context;

/**
 * Created by mafeibiao on 2017/12/8.
 */

public class DisplayUtils {
    private static volatile DisplayUtils instance = null;
    private Context mContext;
    private DisplayUtils(Context context){
        this.mContext = context.getApplicationContext();
    }
    public static DisplayUtils getInstance(Context context){
        if (instance == null){
            synchronized (DisplayUtils.class){
                if (instance ==null){
                    instance = new DisplayUtils(context);
                }
            }
        }

        return instance;
    }

    public int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
