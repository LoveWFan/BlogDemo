package com.poney.blogdemo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.poney.blogdemo.demo1.DemoActivity;
import com.poney.blogdemo.demo1.EGLDemoActivity;
import com.poney.blogdemo.demo2.CameraXActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        requestPermissions();
    }

    private void requestPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        Disposable disposable = rxPermissions.request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO)
                .subscribe(permission -> {
                    if (permission) {
                    } else {
                        Toast.makeText(MainActivity.this, "请授予相关权限", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @OnClick({R.id.gLSurfaceView, R.id.egl, R.id.audio_video})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.gLSurfaceView:
                startActivity(new Intent(this, DemoActivity.class));
                break;
            case R.id.egl:
                startActivity(new Intent(this, EGLDemoActivity.class));
                break;
            case R.id.audio_video:
                startActivity(new Intent(this, CameraXActivity.class));
                break;
        }
    }
}