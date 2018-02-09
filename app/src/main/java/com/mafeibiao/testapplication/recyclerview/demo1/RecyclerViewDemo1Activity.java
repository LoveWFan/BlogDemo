package com.mafeibiao.testapplication.recyclerview.demo1;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mafeibiao.testapplication.DefaultItemDecoration;
import com.mafeibiao.testapplication.MainAdapter;
import com.mafeibiao.testapplication.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewDemo1Activity extends AppCompatActivity {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private RecyclerView.LayoutManager mLayoutManager;
    private List<String> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        ButterKnife.bind(this);

        mLayoutManager = createLayoutManager();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration( new DefaultItemDecoration(ContextCompat.getColor(this, R.color.divider_color)));
        mData = createDataList();

        mRecyclerView.setAdapter(new MainAdapter(this,mData));
    }

    protected List<String> createDataList() {
        List<String> dataList = new ArrayList<>();
        dataList.add("基础使用");
        dataList.add("设置分割线");
        dataList.add("添加HeadView以及FooterView");
        dataList.add("设置EmptyView");
        return dataList;
    }

    protected RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(this);
    }
}
