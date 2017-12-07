package com.mafeibiao.testapplication.MyTagFlowLayout;

import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class MyTagAdapter<T> {
    private List<T> mTagDatas;

    public MyTagAdapter(T[] datas) {
        mTagDatas = new ArrayList<T>(Arrays.asList(datas));
    }

    public int getCount() {
        return mTagDatas == null ? 0 : mTagDatas.size();
    }

    public abstract View getView(MyTagFlowLayout parent, int position, T t);
    public T getItem(int position) {
        return mTagDatas.get(position);
    }
}
