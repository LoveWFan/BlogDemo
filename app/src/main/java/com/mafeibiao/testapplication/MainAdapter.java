package com.mafeibiao.testapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mafeibiao.testapplication.fragment.FragmentActivity;
import com.mafeibiao.testapplication.leakCanary.LeakNavActivity;
import com.mafeibiao.testapplication.messaging.MessageActivity;

import java.util.List;

/**
 * Created by mafeibiao on 2017/12/7.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;

    private Context mContext;
    public MainAdapter(Context context, List<String> data) {
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
                switch (viewHolder.getAdapterPosition()) {
                    case 0: {
                        mContext.startActivity(new Intent(mContext, CustomViewDemoActivity.class));
                        break;
                    }
                    case 1: {
                        mContext.startActivity(new Intent(mContext, CustomViewGroupDemoActivity.class));
                        break;
                    }

                    case 2: {
                        mContext.startActivity(new Intent(mContext, LeakNavActivity.class));
                        break;
                    }
                    case 3: {
                        mContext.startActivity(new Intent(mContext, MessageActivity.class));
                        break;
                    }

                    case 4: {
                        mContext.startActivity(new Intent(mContext, FragmentActivity.class));
                        break;
                    }
                }
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
