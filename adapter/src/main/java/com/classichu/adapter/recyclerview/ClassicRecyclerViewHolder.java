package com.classichu.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by louisgeek on 2017/3/5.
 */

public class ClassicRecyclerViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViewSparseArray = new SparseArray<>();
    private View mItemView;

    public ClassicRecyclerViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
    }

    public <T extends View> T findBindItemView(int viewId) {
        View view = mViewSparseArray.get(viewId);
        if (view == null) {
            view = mItemView.findViewById(viewId);
            mViewSparseArray.put(viewId, view);
        }
        return (T) view;

    }

}
