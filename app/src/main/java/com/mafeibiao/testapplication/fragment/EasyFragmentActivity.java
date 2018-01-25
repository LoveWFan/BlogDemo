package com.mafeibiao.testapplication.fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;

import com.mafeibiao.testapplication.R;

public class EasyFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_fragment);
        Display display = getWindowManager().getDefaultDisplay();
        if (display.getWidth() > display.getHeight()) {
            GoodCarFragment fragment1 = new GoodCarFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment1).commit();
        } else {
            GoodsFragment fragment2 = new GoodsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment2).commit();
        }
    }
}
