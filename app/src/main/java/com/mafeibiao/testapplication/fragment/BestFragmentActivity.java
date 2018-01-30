package com.mafeibiao.testapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.luseen.luseenbottomnavigation.BottomNavigation.OnBottomNavigationItemClickListener;
import com.mafeibiao.testapplication.R;

public class BestFragmentActivity extends AppCompatActivity implements IShow{

    private Fragment mCurFragment = new Fragment();
    private GoodsFragment mGoodsFragment = new GoodsFragment();
    private GoodCarFragment mGoodCarFragment = new GoodCarFragment();
    private TaskFragment mTaskFragment = new TaskFragment();
    private AboutFragment mAboutFragment  = new AboutFragment();
    private CategoryFragment mCategoryFragment  = new CategoryFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_fragment);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        BottomNavigationItem bottomNavigationItem = new BottomNavigationItem
                ("首页", ContextCompat.getColor(this, R.color.firstColor), R.mipmap.ic_account_balance_white_48dp);
        BottomNavigationItem bottomNavigationItem1 = new BottomNavigationItem
                ("分类", ContextCompat.getColor(this, R.color.secondColor), R.mipmap.ic_list_white_48dp);

        BottomNavigationItem bottomNavigationItem2 = new BottomNavigationItem
                ("任务", ContextCompat.getColor(this, R.color.firstColor), R.mipmap.ic_add_circle_outline_white_48dp);
        BottomNavigationItem bottomNavigationItem3 = new BottomNavigationItem
                ("购物车", ContextCompat.getColor(this, R.color.thirdColor), R.mipmap.ic_add_shopping_cart_white_48dp);

        BottomNavigationItem bottomNavigationItem4 = new BottomNavigationItem
                ("我的", ContextCompat.getColor(this, R.color.colorAccent), R.mipmap.ic_account_box_white_48dp);

        bottomNavigationView.addTab(bottomNavigationItem);
        bottomNavigationView.addTab(bottomNavigationItem1);
        bottomNavigationView.addTab(bottomNavigationItem2);
        bottomNavigationView.addTab(bottomNavigationItem3);
        bottomNavigationView.addTab(bottomNavigationItem4);

        bottomNavigationView.setOnBottomNavigationItemClickListener(new OnBottomNavigationItemClickListener() {
            @Override
            public void onNavigationItemClick(int i) {
                switch (i){
                    case 0:
                        switchToHome();
                        break;
                    case 1:
                        switchToCategory();
                        break;
                    case 2:
                        switchToTask();
                        break;
                    case 3:
                        switchToGoodCar();
                        break;
                    case 4:
                        switchToAbout();
                        break;
                }
            }
        });
        switchToHome();
    }

    private void switchFragment(Fragment targetFragment){
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {
            transaction
                    .hide(mCurFragment)
                    .add(R.id.frame_content, targetFragment,targetFragment.getClass().getName())
                    .commit();
        } else {
            transaction
                    .hide(mCurFragment)
                    .show(targetFragment)
                    .commit();
        }
        mCurFragment = targetFragment;


    }


    private void switchToAbout() {
        switchFragment(mAboutFragment);
    }
    private void switchToCategory() {
        switchFragment(mCategoryFragment);
    }
    private void switchToTask() {
        switchFragment(mTaskFragment);
    }
    private void switchToGoodCar() {
        switchFragment(mGoodCarFragment);
    }
    private void switchToHome() {
        switchFragment(mGoodsFragment);
    }



    @Override
    public void show() {

    }
}
