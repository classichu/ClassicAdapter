package com.classichu.classicadapter;

import android.widget.TextView;

import com.classichu.adapter.listview.ClassicBaseAdapter;
import com.classichu.adapter.listview.ClassicBaseViewHolder;

import java.util.List;

/**
 * Created by louisgeek on 2017/3/6.
 */

public class ListViewAdapter extends ClassicBaseAdapter<String> {

    public ListViewAdapter(List<String> mDataList, int mItemLayoutId) {
        super(mDataList, mItemLayoutId);
    }

    @Override
    public void findBindView(int position, ClassicBaseViewHolder classicBaseViewHolder) {
        TextView tv1=classicBaseViewHolder.findBindItemView(android.R.id.text1);
        tv1.setText(mDataList.get(position));
    }

}
