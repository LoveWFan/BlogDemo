package com.mafeibiao.testapplication.LeakCanary;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mafeibiao.testapplication.DefaultItemDecoration;
import com.mafeibiao.testapplication.R;

import java.util.ArrayList;
import java.util.List;

public class LeakNavActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<String> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = createLayoutManager();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration( new DefaultItemDecoration(ContextCompat.getColor(this, R.color.divider_color)));
        mData = createDataList();

        mRecyclerView.setAdapter(new LeakNavAdapter(this,mData));
    }

    protected List<String> createDataList() {
        List<String> dataList = new ArrayList<>();
        dataList.add("静态变量引起的内存泄漏");
        dataList.add("单例模式引起的内存泄漏");
        return dataList;
    }

    protected RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(this);
    }

}
