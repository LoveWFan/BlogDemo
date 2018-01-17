package com.mafeibiao.testapplication.xuliehua;

import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mafeibiao.testapplication.R;
import com.mafeibiao.testapplication.xuliehua.pacel.Book;
import com.mafeibiao.testapplication.xuliehua.pacel.Person;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        Parcel parcel = Parcel.obtain();
        Person person = new Person(23,"TEST",new Book("c++"));

        //写入Parcel
        parcel.writeParcelable(person,0);
        //Parcel读写共用一个位置计数，这里一定要重置一下当前的位置
        parcel.setDataPosition(0);
        //读取Parcel
        Person person1 = parcel.readParcelable(Person.class.getClassLoader());
        Log.d("TestActivity",person1.toString());
    }
}
