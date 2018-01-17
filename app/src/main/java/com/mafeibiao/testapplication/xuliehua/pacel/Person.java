package com.mafeibiao.testapplication.xuliehua.pacel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mafeibiao on 2018/1/2.
 */

public class Person implements Parcelable{
    private int mAge;
    private String mDesc;
    private Book mBook;


    public Person(int age, String desc, Book book) {
        mAge = age;
        mDesc = desc;
        mBook = book;
    }


    protected Person(Parcel in) {
        mAge = in.readInt();
        mDesc = in.readString();
        mBook = in.readParcelable(Thread.currentThread().getContextClassLoader());
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
        dest.writeInt(mAge);
        dest.writeString(mDesc);
        dest.writeParcelable(mBook,0);
    }

    @Override
    public String toString() {
        return "Person{" +
                "mAge=" + mAge +
                ", mDesc='" + mDesc + '\'' +
                '}';
    }
}
