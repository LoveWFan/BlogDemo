package com.mafeibiao.testapplication;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private List<String> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = createLayoutManager();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration( new DefaultItemDecoration(ContextCompat.getColor(this, R.color.divider_color)));
        mData = createDataList();

        mRecyclerView.setAdapter(new MainAdapter(this,mData));
    }

    protected List<String> createDataList() {
        List<String> dataList = new ArrayList<>();
        dataList.add("自定义View");
        dataList.add("自定义ViewGroup");
        dataList.add("内存泄漏");
        dataList.add("Android消息机制");
        dataList.add("Fragment初探");
        dataList.add("Fragment最佳实践");
        dataList.add("Fragment+ViewPager+TabLayout");
        return dataList;
    }

    protected RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
