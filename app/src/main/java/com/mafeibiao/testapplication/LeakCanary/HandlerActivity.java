package com.mafeibiao.testapplication.LeakCanary;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.mafeibiao.testapplication.R;

public class HandlerActivity extends AppCompatActivity {
    private Handler mHandler = new Handler();
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);

        mTextView = (TextView) findViewById(R.id.text);//模拟内存泄露
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTextView.setText("test");
            }
        }, 3 * 60 * 1000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
    }
}
