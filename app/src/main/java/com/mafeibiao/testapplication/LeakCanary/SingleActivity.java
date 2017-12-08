package com.mafeibiao.testapplication.LeakCanary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mafeibiao.testapplication.R;

public class SingleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        DisplayUtils.getInstance(this).dip2px(5);
    }
}
