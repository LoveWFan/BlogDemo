package com.mafeibiao.testapplication.recyclerview.demo1;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mafeibiao.testapplication.DefaultItemDecoration;
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
        setContentView(R.layout.activity_recycler_demo1_view);
        ButterKnife.bind(this);

        mLayoutManager = createLayoutManager();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration( new DefaultItemDecoration(ContextCompat.getColor(this, R.color.divider_color)));
        mData = createDataList();

        mRecyclerView.setAdapter(new RecyclerViewDemo1Adapter(this,mData));
    }

    protected List<String> createDataList() {
        mData = new ArrayList<>();
        for (int i=0;i<20;i++){
            mData.add("这是第"+i+"个View");
        }
        return mData;
    }

    protected RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(this);
    }
}
