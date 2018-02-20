package com.mafeibiao.testapplication.recyclerview.demo3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.TextView;

import com.mafeibiao.testapplication.R;
import com.mafeibiao.testapplication.recyclerview.demo2.RecyclerViewDemo2Adapter;
import com.mafeibiao.testapplication.recyclerview.demo3.advanced.wrapper.EmptyWrapper;
import com.mafeibiao.testapplication.recyclerview.demo3.advanced.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewDemo3Activity extends AppCompatActivity {

    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private List<String> mData;
    private RecyclerViewDemo2Adapter mAdapter;
    private EmptyWrapper mEmptyWrapperAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_domo3);
        ButterKnife.bind(this);

//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        //虚拟数据
        mData = createDataList();

        //设置Adapter必须指定，否则数据怎么显示
        mAdapter = new RecyclerViewDemo2Adapter(mData);

        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);


        TextView t1 = new TextView(this);
        t1.setText("Header 1");
        TextView t2 = new TextView(this);
        t2.setText("Header 2");
        mHeaderAndFooterWrapper.addHeaderView(t1);
        mHeaderAndFooterWrapper.addHeaderView(t2);

        mRecyclerView.setAdapter(mHeaderAndFooterWrapper);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    protected List<String> createDataList() {
        mData = new ArrayList<>();
        for (int i=0;i<20;i++){
            mData.add("这是第"+i+"个View");
        }
        return mData;
    }
}
