package com.mafeibiao.testapplication.fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;

import com.mafeibiao.testapplication.R;

public class EasyFragmentActivity extends AppCompatActivity implements IShow {
    private static String TAG= EasyFragmentActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        setContentView(R.layout.activity_easy_fragment);
        Display display = getWindowManager().getDefaultDisplay();
        if (display.getWidth() > display.getHeight()) {
            GoodCarFragment fragment1 = new GoodCarFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment1,Constant.GOODCAR_FRAGMENT_FLAG).commit();
        } else {
            GoodsFragment fragment2 = new GoodsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment2,Constant.GOODS_FRAGMENT_FLAG).commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG,"onRestart");
    }

    @Override
    public void show(){
        Log.d(TAG,"show");
    }

}
