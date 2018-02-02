package com.mafeibiao.testapplication.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.mafeibiao.testapplication.R;
import com.mafeibiao.testapplication.fragment_tabLayout_viewpager.BaseFragment;


/**
 * Created by Chenyc on 15/7/1.
 */
public class GoodsFragment extends BaseFragment {
    private static String TAG= GoodsFragment.class.getSimpleName();
//    private IShow mCallback;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context != null) {
//            mCallback = (IShow) context;
//        }
        Log.d(TAG,"onAttach");
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Log.d(TAG,"onCreateView");
//        View view = inflater.inflate(R.layout.fragment_goods, null);
////        ((EasyFragmentActivity)getActivity()).show();
////        GoodCarFragment goodCarFragment = (GoodCarFragment) getActivity().getSupportFragmentManager().findFragmentByTag("GOODCAR_FRAGMENT_FLAG");
////        goodCarFragment.show();
//
////        mCallback.show();
//        return view;
//    }


    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if(isVisible){
            //可见，并且是第一次加载

        }else{
            //取消加载
        }
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_goods;
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG,"onViewCreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"onActivityCreated");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG,"onDetach");
    }

}
