package com.mafeibiao.testapplication.recyclerview.demo3;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mafeibiao.testapplication.R;

import java.util.List;


public class RecyclerViewDemo3Adapter extends RecyclerView.Adapter<RecyclerViewDemo3Adapter.ViewHolder> {

    private static final String TAG = RecyclerViewDemo3Adapter.class.getSimpleName();

    private List<String> mData;

    public RecyclerViewDemo3Adapter(List<String> data) {
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.d(TAG,"onCreateViewHolder->viewtype"+viewType);
        View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_menu_main, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder->position"+position);
        holder.setData(this.mData.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //item点击事件
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mData != null ? this.mData.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_title);
        }

        public void setData(String title) {
            this.mTextView.setText(title);
        }
    }
}
