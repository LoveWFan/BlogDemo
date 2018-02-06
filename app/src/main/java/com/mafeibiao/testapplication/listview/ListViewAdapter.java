package com.mafeibiao.testapplication.listview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mafeibiao.testapplication.R;

import java.util.List;

/**
 * Created by mafeibiao on 2018/2/6.
 */

public class ListViewAdapter extends BaseAdapter {
    private static final String TAG = ListViewAdapter.class.getSimpleName();

    private List<String> mList;
    private LayoutInflater inflater;
    private ViewHolder viewHolder;

    public ListViewAdapter(Context context, List<String> list) {
        mList = list;
        this.inflater  = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_view_item_simple, null);
            viewHolder.mTextView=(TextView) convertView.findViewById(R.id.text_view);
            convertView.setTag(viewHolder);

            Log.d(TAG,"convertView == null");
        }else {
            Log.d(TAG,"convertView != null");
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mTextView.setText(mList.get(position));
        return convertView;
    }

    /**
     * Created by mafeibiao on 2018/2/6.
     */

    static class ViewHolder {
        private TextView mTextView;
    }
}
