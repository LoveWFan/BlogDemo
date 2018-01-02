package com.mafeibiao.testapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mafeibiao.testapplication.myTagFlowLayout.MyTagAdapter;
import com.mafeibiao.testapplication.myTagFlowLayout.MyTagFlowLayout;

public class CustomViewGroupDemoActivity extends AppCompatActivity {

    private MyTagFlowLayout mGuseeYourLoveFlowLayout;

    private String[] mGuseeYourLoveVals = new String[]
            {"Android", "Android移动", "Java", "UI设计师", "android实习",
                    "android 移动","android安卓","安卓"};
    private String[] mHotCompanyVals = new String[]
            {"今日头条", "腾讯", "小米", "京东商城", "ofo",
                    "美团点评","百度","爱奇艺","好未来",
            "新浪网","滴滴出行","网易"};
    private MyTagFlowLayout mHotCompanyFlowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_viewgroup);

        mGuseeYourLoveFlowLayout = (MyTagFlowLayout) findViewById(R.id.id_guess_your_love);

        mGuseeYourLoveFlowLayout.setAdapter(new MyTagAdapter<String>(mGuseeYourLoveVals)
        {

            final LayoutInflater mInflater = LayoutInflater.from(CustomViewGroupDemoActivity.this);
            @Override
            public View getView(MyTagFlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.tag_view,
                        mGuseeYourLoveFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        });


        mHotCompanyFlowLayout = (MyTagFlowLayout) findViewById(R.id.id_hot_company);

        mHotCompanyFlowLayout.setAdapter(new MyTagAdapter<String>(mHotCompanyVals)
        {

            final LayoutInflater mInflater = LayoutInflater.from(CustomViewGroupDemoActivity.this);
            @Override
            public View getView(MyTagFlowLayout parent, int position, String s)
            {
                AppCompatTextView tv = (AppCompatTextView) mInflater.inflate(R.layout.tag_view,
                        mHotCompanyFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        });
    }


}
