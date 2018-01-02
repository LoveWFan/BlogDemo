package com.mafeibiao.testapplication.leakCanary;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mafeibiao.testapplication.R;

public class TestActivity extends AppCompatActivity {
    private static Context sContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        sContext = this;
    }
}
