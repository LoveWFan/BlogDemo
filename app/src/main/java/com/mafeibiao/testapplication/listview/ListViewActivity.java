package com.mafeibiao.testapplication.listview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.mafeibiao.testapplication.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListViewActivity extends AppCompatActivity {
    @BindView(R.id.list_view)
    ListView mListView;

    private List<String> mArrayList= new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        ButterKnife.bind(this);
        //初始化数据
        init();

        //创建Adapater
        ListViewAdapter adapter = new ListViewAdapter(this,mArrayList);

        //设置Adapter

        mListView.setAdapter(adapter);
    }

    private void init() {
        for (int i=0;i<20;i++){
            mArrayList.add("这是第"+i+"个View");
        }
    }
}
