package com.mafeibiao.testapplication.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mafeibiao.testapplication.R;
import com.mafeibiao.testapplication.listview.ListViewAdapter;
import com.mafeibiao.testapplication.recyclerview.demo1.RecyclerViewDemo1Activity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GuideMainActivity extends AppCompatActivity {
    @BindView(R.id.list_view)
    ListView mListView;
    private List<String> mArrayList= new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_main);
        //初始化数据
        init();

        //创建Adapater
        ListViewAdapter adapter = new ListViewAdapter(this,mArrayList);

        //设置Adapter

        mListView.setAdapter(adapter);


        //设置item点击监听事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
                        startActivity(new Intent(GuideMainActivity.this, RecyclerViewDemo1Activity.class));
                        break;
                    }

                }
            }
        });
    }

    private void init() {
        mArrayList.add("基础使用");
        mArrayList.add("设置分割线");
        mArrayList.add("添加HeadView以及FooterView");
        mArrayList.add("设置EmptyView");
    }


}
