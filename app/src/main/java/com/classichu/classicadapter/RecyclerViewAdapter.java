package com.classichu.classicadapter;

import android.widget.TextView;

import com.classichu.adapter.recyclerview.ClassicRecyclerViewAdapter;
import com.classichu.adapter.recyclerview.ClassicRecyclerViewHolder;

import java.util.List;

/**
 * Created by louisgeek on 2017/3/6.
 */

public class RecyclerViewAdapter extends ClassicRecyclerViewAdapter<String>{

    public RecyclerViewAdapter(List<String> mDataList, int mItemLayoutId) {
        super(mDataList, mItemLayoutId);
    }

    @Override
    public void findBindView(int position, ClassicRecyclerViewHolder classicRecyclerViewHolder) {
        TextView tv1=classicRecyclerViewHolder.findBindItemView(android.R.id.text1);
        tv1.setText("recycler "+mDataList.get(position));
    }
}
