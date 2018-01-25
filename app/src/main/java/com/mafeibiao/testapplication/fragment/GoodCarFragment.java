package com.mafeibiao.testapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mafeibiao.testapplication.R;


/**
 * Created by Chenyc on 15/7/1.
 */
public class GoodCarFragment extends Fragment {
    private static String TAG= GoodCarFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goodcar, null);

        return view;
    }


    public void show(){
        Log.d(TAG,"show");
    }
}
