package com.mafeibiao.testapplication.recyclerview.demo2;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mafeibiao.testapplication.R;

import java.util.List;

/**
 * 与ListView的Adapter不同，RecyclerView的Adapter需要继承RecyclerView.Adapter<VH>(VH是ViewHolder的类名)
 * 记为RecyclerViewDemo1Adapter。
 * 创建ViewHolder：在RecyclerViewDemo1Adapter中创建一个继承RecyclerView.ViewHolder的静态内部类，记为ViewHolder
 * (RecyclerView必须使用ViewHolder模式，这里的ViewHolder实现几乎与ListView优化时所使用的ViewHolder一致)
 * 在RecyclerViewDemo1Adapter中实现：
 *      VH onCreateViewHolder(ViewGroup parent, int viewType): 映射Item Layout Id，创建VH并返回。
 *      void onBindViewHolder(VH holder, int position): 为holder设置指定数据。
 *      int getItemCount(): 返回Item的个数。
 * 可以看出，RecyclerView将ListView中getView()的功能拆分成了onCreateViewHolder()和onBindViewHolder()。
 */
public class RecyclerViewDemo2Adapter extends RecyclerView.Adapter<RecyclerViewDemo2Adapter.ViewHolder> {

    private List<String> mData;

    public RecyclerViewDemo2Adapter(List<String> data) {
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_menu_main, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
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
