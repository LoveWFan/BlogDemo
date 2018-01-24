package com.mafeibiao.testapplication.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.luseen.luseenbottomnavigation.BottomNavigation.OnBottomNavigationItemClickListener;
import com.mafeibiao.testapplication.R;

public class BestFragmentActivity extends AppCompatActivity {

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


    private void switchToAbout() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new AboutFragment(), Constant.ABOUT_FRAGMENT_FLAG).commit();
    }
    private void switchToCategory() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new CategoryFragment(), Constant.CATEGORY_FRAGMENT_FLAG).commit();
    }
    private void switchToTask() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new TaskFragment(), Constant.TASK_FRAGMENT_FLAG).commit();
    }
    private void switchToGoodCar() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new GoodCarFragment(), Constant.GOODCAR_FRAGMENT_FLAG).commit();
    }
    private void switchToHome() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new GoodFragment(), Constant.GOODS_FRAGMENT_FLAG).commit();
    }


}
