package com.mafeibiao.testapplication.recyclerview.demo1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mafeibiao.testapplication.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewDemo1Activity extends AppCompatActivity {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private List<String> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_demo1_view);
        ButterKnife.bind(this);

        //LayoutManager必须指定，否则无法显示数据,这里指定为线性布局，
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //虚拟数据
        mData = createDataList();
        //设置Adapter必须指定，否则数据怎么显示
        mRecyclerView.setAdapter(new RecyclerViewDemo1Adapter(mData));
    }

    protected List<String> createDataList() {
        mData = new ArrayList<>();
        for (int i=0;i<20;i++){
            mData.add("这是第"+i+"个View");
        }
        return mData;
    }

}
