package com.mafeibiao.testapplication.recyclerview.demo1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mafeibiao.testapplication.R;

import java.util.List;

/**
 * Created by mafeibiao on 2017/12/7.
 */

public class RecyclerViewDemo1Adapter extends RecyclerView.Adapter<RecyclerViewDemo1Adapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;

    private Context mContext;
    public RecyclerViewDemo1Adapter(Context context, List<String> data) {
        this.mData = data;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ViewHolder viewHolder = new ViewHolder(mInflater.inflate(R.layout.item_menu_main, parent, false));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"点击了第"+viewHolder.getAdapterPosition()+"个位置",Toast.LENGTH_SHORT).show();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(this.mData.get(position));
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
