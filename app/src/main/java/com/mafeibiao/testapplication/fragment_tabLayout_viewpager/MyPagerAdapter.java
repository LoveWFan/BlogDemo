package com.mafeibiao.testapplication.fragment_tabLayout_viewpager;

/**
 * Created by mafeibiao on 2018/2/2.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mafeibiao.testapplication.fragment.CategoryFragment;
import com.mafeibiao.testapplication.fragment.GoodsFragment;
import com.mafeibiao.testapplication.fragment.TaskFragment;

public class MyPagerAdapter extends FragmentPagerAdapter {
    //fragment的数量
    int nNumOfTabs;
    public MyPagerAdapter(FragmentManager fm, int nNumOfTabs)
    {
        super(fm);
        this.nNumOfTabs=nNumOfTabs;
    }

    /**
     * 重写getItem方法
     *
     * @param position 指定的位置
     * @return 特定的Fragment
     */
    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                GoodsFragment tab1=new GoodsFragment();
                return tab1;
            case 1:
                CategoryFragment tab2=new CategoryFragment();
                return tab2;
            case 2:
                TaskFragment tab3=new TaskFragment();
                return tab3;
        }
        return null;
    }

    /**
     * 重写getCount方法
     *
     * @return fragment的数量
     */
    @Override
    public int getCount() {
        return nNumOfTabs;
    }
}

