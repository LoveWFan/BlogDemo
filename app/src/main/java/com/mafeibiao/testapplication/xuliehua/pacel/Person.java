package com.mafeibiao.testapplication.xuliehua.pacel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mafeibiao on 2018/1/2.
 */

public class Person implements Parcelable{
    private String desc;
    private Person(Parcel in) {
        desc = in.readString();
    }

    public Person(String desc) {
        this.desc = desc;
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(desc);
    }
}
