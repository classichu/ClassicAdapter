package com.classichu.adapter.listview;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by louisgeek on 2017/3/5.
 */

public class ClassicBaseViewHolder {
    private SparseArray<View> mViewSparseArray = new SparseArray<>();
    protected View mItemView;


    public ClassicBaseViewHolder(View itemView) {
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
