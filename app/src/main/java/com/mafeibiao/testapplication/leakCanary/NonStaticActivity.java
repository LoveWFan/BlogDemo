package com.mafeibiao.testapplication.leakCanary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mafeibiao.testapplication.R;

public class NonStaticActivity extends AppCompatActivity {
    private static Config sConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_static);
        sConfig = new Config();

    }

    class Config {

    }

}
