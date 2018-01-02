package com.mafeibiao.testapplication.messaging;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mafeibiao.testapplication.R;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //模拟耗时操作
                    Thread.sleep(3* 60 * 1000);
                    //在子线程中更新UI之前，先准备一个Looper，与主线程相同
                    if (Looper.myLooper() != null){
                        Looper.prepare();
                    }
                    //耗时操作完成之后显示一个通知
                    Toast.makeText(MessageActivity.this,"test",Toast.LENGTH_SHORT).show();

                    //无限循环
                    Looper.loop();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
